import 'package:firebase_crashlytics/firebase_crashlytics.dart' deferred as crash;

import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/frame.dart';

class Observatory extends NavigatorObserver{

  List<Route> routeHistory = [];
  SW app;
  GlobalKey<FrameState> frame;

  Observatory(this.app, this.frame);

  String currentRoute(){
    if(routeHistory.isNotEmpty) return routeHistory.last.settings.name ?? "";
    return "";
  }

  void report(){
    if(routeHistory.isNotEmpty){
      frame.currentState?.selected = routeHistory.last.settings.name ?? "";
    }
    if(app.crashReporting){
      crash.loadLibrary().then((value) => crash.FirebaseCrashlytics.instance.setCustomKey("page", routeHistory.last.settings.name ?? "unknown"));
    }
  }

  @override
  void didPush(Route route, Route? previousRoute) {
    routeHistory.add(route);
    super.didPush(route, previousRoute);
    report();
  }

  @override
  void didPop(Route route, Route? previousRoute) {
    routeHistory.removeLast();
    super.didPop(route, previousRoute);
    report();
  }

  @override
  void didRemove(Route route, Route? previousRoute) {
    var beginIndex = routeHistory.indexOf(route);
    var endIndex = -1;
    if (previousRoute != null){
      endIndex = routeHistory.indexOf(previousRoute);
    }
    if((endIndex == -1 && beginIndex != -1) || beginIndex -1 == endIndex){
      routeHistory.remove(route);
    }else if(endIndex != -1 && beginIndex != -1){
      routeHistory.removeRange(beginIndex,endIndex);
    }
    super.didRemove(route, previousRoute);
    report();
  }

  @override
  void didReplace({Route? newRoute, Route? oldRoute}) {
    if (oldRoute != null && newRoute != null && routeHistory.contains(oldRoute)){
      routeHistory[routeHistory.indexOf(oldRoute)] = newRoute;
    }
    super.didReplace(newRoute: newRoute, oldRoute: oldRoute);
    report();
  }

  //Returns the route if it is in history, otherwise it return null.
  //It preferes the route give, then settings, then the name
  Route? containsRoute({Route? route, RouteSettings? settings, String? name}){
    if(route != null){
      return routeHistory.contains(route) ? route : null;
    }
    if(settings != null){
      try{
        return routeHistory.firstWhere(
          (element) => element.settings == settings,
        );
      } catch (e){
        if(e is StateError){
          return null;
        }
      }
    }
    if(name != null){
      try{
        return routeHistory.firstWhere(
          (element) => element.settings.name == name,
        );
      } catch (e){
        if(e is StateError){
          return null;
        }
      }
    }
    return null;
  }
}