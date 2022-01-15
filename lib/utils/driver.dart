import 'package:google_sign_in/google_sign_in.dart';
import 'package:googleapis/drive/v3.dart';
import 'package:extension_google_sign_in_as_googleapis_auth/extension_google_sign_in_as_googleapis_auth.dart';

class Driver{
  //TODO: Google Drive Everything

  String? wd;
  DriveApi? api;
  GoogleSignIn? gsi;

  bool initialized = false;

  bool isReady() => isSignedIn() && initialized;

  bool isSignedIn() =>
    gsi != null && gsi!.currentUser != null;

  Future<bool> init() async{
    if(isSignedIn()){
      api = DriveApi((await gsi!.authenticatedClient())!);
      return true;
    }
    gsi = GoogleSignIn(scopes: [DriveApi.driveScope]);
    try{
      await gsi!.signInSilently();
      if (gsi!.currentUser == null){
        await gsi!.signIn();
      }
    }catch(e){
      print(e);
      return false;
    }
    if (gsi!.currentUser == null){
      gsi = null;
      return false;
    }
    api = DriveApi((await gsi!.authenticatedClient())!);
    initialized = true;
    return true;
  }

  void setWD(String filename){}
  String? createFile(String filename){}
  void createFolder(String filename){}
  String? getIDFromRoot(String filename){}
  String? getID(String filename){}
}