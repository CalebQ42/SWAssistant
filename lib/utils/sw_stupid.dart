import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:http/http.dart';
import 'package:stupid/stupid.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:swassistant/sw.dart';
import 'package:uuid/uuid.dart';

class SWStupid extends Stupid{
  SW app;

  SWStupid(this.app, String apiKey, String uuid) : super(
    baseUrl: Uri.parse("https://api.darkstorm.tech"),
    deviceId: uuid,
    apiKey: apiKey,
  );

  Future<UploadResponse> uploadProfile(Editable ed) async{
    try{
      var outBod = const JsonEncoder().convert(ed.toJson());
      if(outBod.codeUnits.length > 5242880){
        return UploadResponse(statusCode: 413);
      }
      print(baseUrl.resolveUri(
          Uri(
            path: "/profile/upload",
            queryParameters: {
              "key": apiKey,
              "type": (){
                if(ed is Character){
                  return "character";
                }else if(ed is Minion){
                  return "minion";
                }
                return "vehicle";
              }()
            }
          )
        ).toString());
      var resp = await post(
        baseUrl.resolveUri(
          Uri(
            path: "/profile/upload",
            queryParameters: {
              "key": apiKey,
              "type": (){
                if(ed is Character){
                  return "character";
                }else if(ed is Minion){
                  return "minion";
                }
                return "vehicle";
              }()
            }
          )
        ),
        headers: <String, String>{
          "Content-Type": "application/json"
        },
        body: outBod
      );
      var out = UploadResponse(
        statusCode: resp.statusCode,
      );
      if(resp.statusCode != 201) return out;
      Map<String, dynamic> body = const JsonDecoder().convert(resp.body);
      out.id = body["id"];
      out.expiration = DateTime.fromMillisecondsSinceEpoch(body["expiration"]*1000);
      return out;
    }catch(e, stack){
      if(kDebugMode){
        print("$e\n$stack");
      }else{
        app.stupid?.crash(Crash(
          error: e.toString(),
          stack: stack.toString(),
          version: app.package.version
        ));
      }
    }
    return UploadResponse(statusCode: 404);
  }

  Future<Editable?> downloadProfile(String id) async{
    try{
      var resp = await get(
        baseUrl.resolveUri(
          Uri(
            path: "/profile/$id",
            queryParameters: <String, String>{
              "key": apiKey,
            }
          )
        )
      );
      if(resp.statusCode != 200 || resp.body.isEmpty) return null;
      Map<String, dynamic> values = const JsonDecoder().convert(resp.body);
      if(values["uid"] == null) values["uid"] = const Uuid().v4();
      switch(values["type"]){
        case "character":
          return Character(app: app)..loadJson(values, app.prefs.subtractMode);
        case "minion":
          return Minion(app: app)..loadJson(values, app.prefs.subtractMode);
        default:
          return Vehicle(app: app)..loadJson(values, app.prefs.subtractMode);
      }
    }catch(e, stack){
      if(kDebugMode){
        print("$e\n$stack");
      }else{
        app.stupid?.crash(Crash(
          error: e.toString(),
          stack: stack.toString(),
          version: app.package.version
        ));
      }
    }
    return null;
  }
}

class UploadResponse{
  String? id;
  int statusCode;
  DateTime? expiration;

  UploadResponse({
    this.id,
    required this.statusCode
  });

  UploadResponse.timeout() : statusCode = 408;

  bool isTooLarge() => statusCode == 413;
  bool isSuccess() => statusCode == 201;
  bool isServerError() => statusCode == 500;
  bool isNotFound() => statusCode == 404;
  bool isTimeout() => statusCode == 408;
}