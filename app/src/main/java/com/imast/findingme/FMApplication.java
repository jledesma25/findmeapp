package com.imast.findingme;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by aoki on 07/08/2015.
 */
public class FMApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "VxoGtmV3iqI6AlrSE1Q1FRgCSG1eHXEEDtNcLWq9", "yxhBk5gegYeagwQxRRPK6gSMb3VNVtt758LRu0nD");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
