package com.apps.darkstorm.swrpg.assistant;

import android.app.Application;
import android.content.SharedPreferences;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveFolder;

public class SWrpg extends Application{
    public GoogleApiClient gac = null;
    public DriveFolder charsFold = null;
    public String defaultLoc = "";
    public boolean askingPerm = false;
    public boolean driveFail = false;
    public SharedPreferences prefs = null;
    public boolean bought = false;

    IInAppBillingService iaps;
}
