package com.airwatch.roomfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.airwatch.login.SDKAppAuthenticator;
import com.airwatch.login.timeout.SDKSessionManagerInternal;
import com.airwatch.sdk.AirWatchSDKBaseIntentService;
import com.airwatch.sdk.context.SDKContext;
import com.airwatch.sdk.context.SDKContextManager;
import com.airwatch.sdk.profile.AnchorAppStatus;
import com.airwatch.sdk.profile.ApplicationProfile;


public class AirWatchSDKIntentService extends AirWatchSDKBaseIntentService {

    private static final String TAG = "AirWatchSDKIntent";
    private static final String CLEAR_APP_DATA_BROADCAST = "com.airwatch.roomfinder.clearappdata.BROADCAST";

    @Override
    protected void onClearAppDataCommandReceived(Context context) {
        Intent clearAppDataIntent = new Intent(CLEAR_APP_DATA_BROADCAST);
        LocalBroadcastManager.getInstance(context).sendBroadcast(clearAppDataIntent);
        SDKContextManager.getSDKContext().init(context);
        SDKContextManager.getSDKContext().getSDKSecurePreferences().edit().clear();
    }

    @Override
    protected void onAnchorAppStatusReceived(Context ctx, AnchorAppStatus appStatus) {
        if(appStatus.getWorkspaceExitMode() == 0
                && SDKContextManager.getSDKContext().getCurrentState() != SDKContext.State.IDLE){
            SDKSessionManagerInternal manger = SDKSessionManagerInternal.getsSdkSessionDelegate(ctx);
            if(SDKAppAuthenticator.AUTHENTICATION_MODE_SSO.equalsIgnoreCase(manger.getAuthenticationMode())){
                manger.handleAuthenticationTimeOut();
            }
        }
    }

    @Override
    protected void onAnchorAppUpgrade(Context ctx, boolean isUpgrade) {
        Log.d(TAG, "Received AnchorAppUpgrade broadcast. AnchorApp is " + (isUpgrade ? "upgrading" : "removed"));
    }

    @Override
    protected void onApplicationConfigurationChange(Bundle applicationConfiguration) {
        Log.d(TAG, "received application configuration change "+applicationConfiguration);
    }

    @Override
    protected void onApplicationProfileReceived(Context context, String s, ApplicationProfile applicationProfile) {
        Log.d(TAG, "received application profile");
    }
}