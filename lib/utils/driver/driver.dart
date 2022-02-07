import 'dart:async';

import 'package:google_sign_in/google_sign_in.dart';
import 'package:googleapis/drive/v3.dart';
import 'package:extension_google_sign_in_as_googleapis_auth/extension_google_sign_in_as_googleapis_auth.dart';
import 'package:simple_connection_checker/simple_connection_checker.dart';
import 'package:swassistant/utils/driver/query.dart';

class Driver{
  String wd = "appDataFolder";
  DriveApi? api;
  GoogleSignIn? gsi;

  StreamSubscription<bool>? sub;
  bool internetAvailable = true;

  //ready returns whether the driver is ready to use. If the driver is not ready, it tries to initialize it.
  Future<bool> ready([String? wdFolder]) async {
    if(sub == null){
      var checker = SimpleConnectionChecker();
      sub = checker.onConnectionChange.listen((event) {
        internetAvailable = event;
      });
      if(!await SimpleConnectionChecker.isConnectedToInternet()) return false;
    }
    if(!internetAvailable) return false;
    if(gsi != null && gsi!.currentUser != null && api != null){
      if(!(await gsi!.isSignedIn())){
        if(gsi!.currentUser == null || !(await gsi!.isSignedIn())){
          await gsi!.signInSilently();
          if(gsi!.currentUser == null) await gsi!.signIn();
        }
        if(gsi!.currentUser == null || !(await gsi!.isSignedIn())) return false;
      }
      api = DriveApi((await gsi!.authenticatedClient())!);
      return true;
    }
    try{
      gsi ??= GoogleSignIn(scopes: [DriveApi.driveAppdataScope]);
      if(gsi!.currentUser == null || !(await gsi!.isSignedIn())){
        await gsi!.signInSilently();
        if(gsi!.currentUser == null) await gsi!.signIn();
      }
      if(gsi!.currentUser == null) return false;
      api = DriveApi((await gsi!.authenticatedClient())!);
    } catch(e) {
      return false;
    }
    if(wdFolder != null) await setWD(wdFolder);
    return true;
  }

  //readySync is similar to ready, except does NOT try to initialize the driver and can be used syncronysly. Use ready when possible.
  bool readySync() =>
    internetAvailable && gsi != null && gsi!.currentUser != null && api != null;

  Future<bool> setWD(String folder) async {
    if(!await ready()) return false;
    if(folder == "" || folder == "/"){
      wd = "appDataFolder";
      return true;
    }
    var foldId = await getIDFromRoot(folder, mimeType: DriveQueryBuilder.folderMime, createIfMissing: true);
    if (foldId == null) return false;
    wd = foldId;
    return true;
  }

  Future<List<File>?> listFilesFromRoot(String folder) async{
    if(!await ready()) return null;
    var foldID = await getIDFromRoot(folder, mimeType: DriveQueryBuilder.folderMime);
    if(foldID == null) return null;
    return (await api!.files.list(
      spaces: "appDataFolder",
      q: "'" + foldID + "' in parents"
    )).files;
  }

  Future<List<File>?> listFiles(String folder) async {
    if(!await ready()) return null;
    var foldID = await getID(folder, mimeType: DriveQueryBuilder.folderMime);
    if(foldID == null) return null;
    return (await api!.files.list(
      spaces: "appDataFolder",
      q: "'" + foldID + "' in parents"
    )).files;
  }

  Future<String?> getIDFromRoot(String filename, {String? mimeType, bool createIfMissing = false}) async {
    if(!await ready()) return null;
    if(filename == "" || filename == "/") return "appDataFolder";
    var parentID = "appDataFolder";
    var split = filename.split("/");
    List<File>? out;
    for(int i = 0; i< split.length; i++){
      var fold = split[i];
      if(fold == "") continue;
      var query = DriveQueryBuilder();
      if(i != split.length -1){
        query.mime = DriveQueryBuilder.folderMime;
      }else{
        query.mime = mimeType;
      }
      query.name = fold;
      query.parent = parentID;
      out = (await api!.files.list(
        spaces: "appDataFolder",
        q: query.getQuery()
      )).files;
      if (out == null || out.isEmpty) {
        if (!createIfMissing) return null;
        var id = await createFileWithParent(fold, parentID, mimeType: query.mime);
        if (id == null) return null;
        parentID = id;
        var fil = await getFile(id);
        if(fil == null) return null;
        out = [fil];
        continue;
      }
      if(out[0].id == null) {
        if (!createIfMissing) return null;
        var id = await createFileWithParent(fold, parentID, mimeType: query.mime);
        if (id == null) return null;
        parentID = id;
        var fil = await getFile(id);
        if(fil == null) return null;
        out = [fil];
        continue;
      }
      parentID = out[0].id!;
    }
    return out![0].id!;
  }

  Future<String?> getID(String filename, {String? mimeType, bool createIfMissing = false}) async {
    if(!await ready()) return null;
    if(filename == "" || filename == "/") return wd;
    var parentID = wd;
    var split = filename.split("/");
    List<File>? out;
    for(int i = 0; i< split.length; i++){
      var fold = split[i];
      if(fold == "") continue;
      var query = DriveQueryBuilder();
      if(i != split.length -1){
        query.mime = DriveQueryBuilder.folderMime;
      }else{
        query.mime = mimeType;
      }
      query.name = fold;
      query.parent = parentID;
      out = (await api!.files.list(
        spaces: "appDataFolder",
        q: query.getQuery()
      )).files;
      if (out == null || out.isEmpty) {
        if (!createIfMissing) return null;
        var id = await createFileWithParent(fold, parentID);
        if (id == null) return null;
        parentID = id;
        continue;
      }
      if(out[0].id == null) {
        if (!createIfMissing) return null;
        var id = await createFileWithParent(fold, parentID);
        if (id == null) return null;
        parentID = id;
        continue;
      }
      parentID = out[0].id!;
    }
    return out![0].id;
  }

  Future<String?> createFolderFromRoot(String filename, {String? description}) async =>
    createFileFromRoot(filename, description: description, mimeType: DriveQueryBuilder.folderMime);

  Future<String?> createFolder(String filename, {String? description}) async =>
    createFile(filename, description: description, mimeType: DriveQueryBuilder.folderMime);

  Future<String?> createFileFromRoot(String filename, {String? mimeType, Map<String, String?>? appProperties, String? description}) async{
    if(!await ready()) return null;
    String? parent = 'appDataFolder';
    var lastInd = filename.lastIndexOf("/");
    if(lastInd != -1){
      parent = await getIDFromRoot(filename.substring(0,lastInd));
      if(parent == null) return null;
    }
    var fil = File(
      modifiedTime: DateTime.now(),
      appProperties: appProperties,
      description: description,
      parents: [parent],
      name: filename.substring(lastInd+1),
      mimeType: mimeType,
    );
    fil = await api!.files.create(fil);
    return fil.id;
  }

  Future<String?> createFileWithParent(String filename, String parentId, {String? mimeType, Map<String, String?>? appProperties, String? description}) async {
    if(!await ready()) return null;
    var fil = File(
      modifiedTime: DateTime.now(),
      appProperties: appProperties,
      description: description,
      parents: [parentId],
      name: filename,
      mimeType: mimeType,
    );
    fil = await api!.files.create(fil);
    return fil.id;
  }

  Future<String?> createFile(String filename, {String? mimeType, Map<String, String?>? appProperties, String? description, Stream<List<int>>? data, int? dataLength}) async{
    if(!await ready()) return null;
    String? parent = wd;
    var lastInd = filename.lastIndexOf("/");
    if(lastInd != -1){
      parent = await getID(filename.substring(0,lastInd));
      if(parent == null) return null;
    }
    var fil = File(
      modifiedTime: DateTime.now(),
      appProperties: appProperties,
      description: description,
      parents: [parent],
      name: filename.substring(lastInd+1),
      mimeType: mimeType,
    );
    fil = await api!.files.create(
      fil,
      uploadMedia: (data != null) ? Media(data, dataLength) : null
    );
    return fil.id;
  }

  Future<File?> getFile(String id) async {
    if(!await ready()) return null;
    return (await api!.files.get(id)) as File;
  }

  Future<Media?> getContents(String id) async {
    if(!await ready()) return null;
    return (await api!.files.get(id, downloadOptions: DownloadOptions.fullMedia)) as Media;
  }

  Future<bool> updateContents(String id, Stream<List<int>> data, {int? dataLength}) async{
    if(!await ready()) return false;
    var fil = await api!.files.update(
      File(modifiedTime: DateTime.now(),), id,
      uploadMedia: Media(data, dataLength)
    );
    return fil.id != null;
  }

  Future<void> delete(String id) async {
    if(!await ready()) return Future.value();
    return await api!.files.delete(id);
  }

  Future<bool> trash(String id) async {
    if(!await ready()) return false;
    return (await api!.files.update(File(trashed: true), id)).trashed ?? false;
  }

  Future<bool> unTrash(String id) async {
    if(!await ready()) return false;
    return !((await api!.files.update(File(trashed: false), id)).trashed ?? false);
  }
}