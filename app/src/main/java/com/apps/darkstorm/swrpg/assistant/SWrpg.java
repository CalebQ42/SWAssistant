package com.apps.darkstorm.swrpg.assistant;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveFolder;
import com.google.firebase.analytics.FirebaseAnalytics;

public class SWrpg extends Application{
    public GoogleApiClient gac = null;
    public DriveFolder charsFold = null;
    public String defaultLoc = "";
    public boolean askingPerm = false;
    public boolean driveFail = false;
    public SharedPreferences prefs = null;
    public boolean bought = false;
    public FirebaseAnalytics fa = null;

    IInAppBillingService iaps;

    public void logEvent(String name,String value){
        if (value == null)
            value = "";
        if (!prefs.getBoolean(getString(R.string.analytics),true))
            return;
        if(fa == null)
            fa = FirebaseAnalytics.getInstance(this);
        Bundle bund = new Bundle();
        bund.putString(FirebaseAnalytics.Param.ITEM_NAME,name);
        if (!value.equals(""))
            bund.putString(FirebaseAnalytics.Param.VALUE,value);
        fa.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bund);
    }
}
