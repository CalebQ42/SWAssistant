package com.apps.darkstorm.swrpg;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveFolder;

public class SWrpg extends Application{
    public GoogleApiClient gac = null;
    public DriveFolder charsFold = null;
    public DriveFolder vehicFold = null;
    public String defaultLoc = "";
    public boolean askingPerm = false;
    public SharedPreferences prefs = null;
}
