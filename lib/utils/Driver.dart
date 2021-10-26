import 'package:google_sign_in/google_sign_in.dart';
import 'package:googleapis/drive/v3.dart';
import 'package:extension_google_sign_in_as_googleapis_auth/extension_google_sign_in_as_googleapis_auth.dart';

class Driver{

  String? wd;
  DriveApi? api;
  GoogleSignIn gsi;

  Driver(this.gsi);

  void initGsi() async =>
    api = DriveApi((await gsi.authenticatedClient())!);

  void setWD(String filename){}
  String? createFile(String filename){}
  void createFolder(String filename){}
  String? getIDFromRoot(String filename){}
  String? getID(String filename){}
}