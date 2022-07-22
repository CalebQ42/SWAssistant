class DriveQueryBuilder{
  static const String folderMime = "application/vnd.google-apps.folder";
  static const String fileMime = "application/vnd.google-apps.file";

  static const String orderCreation = "createdTime";
  static const String orderFolder = "folder";
  static const String orderModMe = "modifiedByMeTime";
  static const String orderMod = "modifiedTime";
  static const String orderName = "name";
  static const String orderSize = "quotaBytesUsed";
  static const String orderRecent = "recency";
  static const String orderSharedTime = "sharedWithMeTime";
  static const String orderStarred = "starred";
  static const String orderViewedByMe = "viewedByMeTime";

  String? name;
  String? mime;
  String? notMime;
  String? parent;
  List<String>? nameContains;
  List<String>? nameNotContains;
  List<String>? fullTextContains;
  List<String>? fullTextNotContains;
  DateTime? editedAfter;
  Map<String, String>? appProperties;
  bool trashed = false;

  String getQuery(){
    String out = "";
    if(name != null) out = _append(out, "name = '${_escape(name!)}'");
    if(mime != null) out = _append(out, "mimeType = '${_escape(mime!)}'");
    if(notMime != null) out = _append(out, "mimeType != '${_escape(notMime!)}'");
    if(parent != null) out = _append(out, "'${_escape(parent!)}' in parents");
    out = _append(out, "trashed = $trashed");
    if(nameContains != null){
      for(var cont in nameContains!){
        out = _append(out, "name contains '${_escape(cont)}'");
      }
    }
    if(nameNotContains != null){
      for(var cont in nameNotContains!){
        out = _append(out, "not name contains '${_escape(cont)}'");
      }
    }
    if(fullTextContains != null){
      for(var cont in fullTextContains!){
        out = _append(out, "fullText contains '${_escape(cont)}'");
      }
    }
    if(fullTextNotContains != null){
      for(var cont in fullTextNotContains!){
        out = _append(out, "not fullText contains '${_escape(cont)}'");
      }
    }
    if(editedAfter != null){
      out = _append(out, "modifiedTime > ${_dateToString(editedAfter!)}");
    }
    if(appProperties != null){
      for(var key in appProperties!.keys){
        out = _append(out, "appProperties has { key='${_escape(key)}");
        if(appProperties![key] != null){
          out += "' and value='${_escape(appProperties![key]!)}";
        }
        out += "' }";
      }
    }
    return out;
  }

  String _dateToString(DateTime time){
    time = time.toUtc();
    return "${time.year}-${time.month}-${time.day}T${time.hour}:${time.minute}:${time.second}";
  }

  String _escape(String toEscape) =>
    toEscape.replaceAll("'", "\\'").replaceAll("\\", "\\\\");

  String _append(String oldQuery, String newQuery){
    if(oldQuery != ""){
      return "$oldQuery and $newQuery";
    }
    return newQuery;
  }
}