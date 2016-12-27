package com.apps.darkstorm.swrpg;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;

public class SWrpg extends Application {
    public GoogleApiClient gac = null;
    public boolean initConnect = false;
}
