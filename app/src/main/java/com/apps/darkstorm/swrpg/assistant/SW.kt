package com.apps.darkstorm.swrpg.assistant

import android.app.Application
import android.content.SharedPreferences
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.drive.DriveClient
import com.google.android.gms.drive.DriveResourceClient

class SW: Application() {
    lateinit var drc: DriveResourceClient
    lateinit var dc: DriveClient
    lateinit var gsi: GoogleSignIn

    lateinit var prefs: SharedPreferences
}