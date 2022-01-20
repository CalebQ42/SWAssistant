import 'package:google_sign_in/google_sign_in.dart';
import 'package:googleapis/drive/v3.dart';
import 'package:extension_google_sign_in_as_googleapis_auth/extension_google_sign_in_as_googleapis_auth.dart';
import 'package:swassistant/utils/driver/query.dart';

class Driver {
  //TODO: Google Drive Everything

  String wd = "root";
  DriveApi? api;
  GoogleSignIn? gsi;

  bool initialized = false;

  bool isReady() => isSignedIn() && initialized;

  bool isSignedIn() => gsi != null && gsi!.currentUser != null;

  Future<bool> init() async {
    if (isSignedIn()) {
      api = DriveApi((await gsi!.authenticatedClient())!);
      return true;
    }
    gsi = GoogleSignIn(scopes: [DriveApi.driveScope]);
    try {
      await gsi!.signInSilently();
      if (gsi!.currentUser == null) {
        await gsi!.signIn();
      }
    } catch (e) {
      return false;
    }
    if (gsi!.currentUser == null) {
      gsi = null;
      return false;
    }
    api = DriveApi((await gsi!.authenticatedClient())!);
    initialized = true;
    return true;
  }

  Future<List<File>?> listFilesFromRoot(String folder) async {
    if (!isReady()) return null;
    var foldID =
        await getIDFromRoot(folder, mimeType: DriveQueryBuilder.folderMime);
    if (foldID == null) return null;
    return (await api!.files
            .list(corpora: "user", q: "'" + foldID + "' in parents"))
        .files;
  }

  Future<List<File>?> listFiles(String folder) async {
    if (!isReady()) return null;
    var foldID = await getID(folder, mimeType: DriveQueryBuilder.folderMime);
    if (foldID == null) return null;
    return (await api!.files
            .list(corpora: "user", q: "'" + foldID + "' in parents"))
        .files;
  }

  Future<bool> setWD(String folder) async {
    if (!isReady()) return false;
    if (folder == "" || folder == "/") {
      wd = "root";
      return true;
    }
    var foldID =
        await getIDFromRoot(folder, mimeType: DriveQueryBuilder.folderMime);
    if (foldID == null) return false;
    wd = foldID;
    return true;
  }

  Future<String?> getIDFromRoot(String filename, {String? mimeType}) async {
    if (!isReady()) return null;
    if (filename == "" || filename == "/") return "root";
    var parentID = "root";
    var split = filename.split("/");
    List<File>? out;
    for (int i = 0; i < split.length; i++) {
      var fold = split[i];
      if (fold == "") continue;
      var query = DriveQueryBuilder();
      if (i != split.length - 1) {
        query.mime = DriveQueryBuilder.folderMime;
      } else {
        query.mime = mimeType;
      }
      query.name = fold;
      query.parent = parentID;
      out = (await api!.files.list(corpora: "user", q: query.getQuery())).files;
      if (out == null || out.isEmpty) return null;
      if (out[0].id == null) return null;
      parentID = out[0].id!;
    }
    if (mimeType != null && out![0].mimeType != mimeType) {
      for (var otherOut in out) {
        if (otherOut.mimeType == mimeType) return otherOut.id;
      }
      return null;
    }
    return out![0].id!;
  }

  Future<String?> getID(String filename, {String? mimeType}) async {
    if (!isReady()) return null;
    if (filename == "" || filename == "/") return wd;
    var parentID = wd;
    var split = filename.split("/");
    List<File>? out;
    for (int i = 0; i < split.length; i++) {
      var fold = split[i];
      if (fold == "") continue;
      var query = DriveQueryBuilder();
      if (i != split.length - 1) {
        query.mime = DriveQueryBuilder.folderMime;
      } else {
        query.mime = mimeType;
      }
      query.name = fold;
      query.parent = parentID;
      out = (await api!.files.list(corpora: "user", q: query.getQuery())).files;
      if (out == null || out.isEmpty) return null;
      if (out[0].id == null) return null;
      parentID = out[0].id!;
    }
    return out![0].id;
  }

  Future<String?> createFolderFromRoot(String filename,
          {String? description}) async =>
      createFileFromRoot(filename,
          description: description, mimeType: DriveQueryBuilder.folderMime);
  Future<String?> createFolder(String filename, {String? description}) async =>
      createFile(filename,
          description: description, mimeType: DriveQueryBuilder.folderMime);
  Future<String?> createFileFromRoot(String filename,
      {String? mimeType,
      Map<String, String?>? appProperties,
      String? description}) async {
    if (!isReady()) return null;
    String? parent = 'root';
    var lastInd = filename.lastIndexOf("/");
    if (lastInd != -1) {
      parent = await getIDFromRoot(filename.substring(0, lastInd));
      if (parent == null) return null;
    }
    var fil = File(
      appProperties: appProperties,
      description: description,
      parents: [parent],
      name: filename.substring(lastInd + 1),
      mimeType: mimeType,
    );
    fil = await api!.files.create(fil);
    return fil.id;
  }

  Future<String?> createFile(String filename,
      {String? mimeType,
      Map<String, String?>? appProperties,
      String? description,
      Stream<List<int>>? data,
      int? dataLength}) async {
    if (!isReady()) return null;
    String? parent = wd;
    var lastInd = filename.lastIndexOf("/");
    if (lastInd != -1) {
      parent = await getID(filename.substring(0, lastInd));
      if (parent == null) return null;
    }
    var fil = File(
      appProperties: appProperties,
      description: description,
      parents: [parent],
      name: filename.substring(lastInd + 1),
      mimeType: mimeType,
    );
    fil = await api!.files.create(fil,
        uploadMedia: (data != null) ? Media(data, dataLength) : null);
    return fil.id;
  }

  Future<bool> updateContents(String id, Stream<List<int>> data,
      {int? dataLength}) async {
    if (!isReady()) return false;
    var fil = await api!.files
        .update(File(), id, uploadMedia: Media(data, dataLength));
    return fil.id != null;
  }
}
