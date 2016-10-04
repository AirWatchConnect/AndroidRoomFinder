package com.airwatch.roomfinder;

import android.app.Application;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.airwatch.app.AWApplication;
import com.airwatch.gateway.ui.GatewaySplashActivity;
import com.airwatch.roomfinder.network.INetworkRequest;
import com.airwatch.roomfinder.network.NetworkRequest;
import com.airwatch.roomfinder.network.NetworkRequestFactory;


public class RoomFinderApp extends AWApplication implements NetworkRequestFactory {

    private INetworkRequest networkRequest = new NetworkRequest();

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
    }

    @Override
    public Intent getMainActivityIntent() {
        return new Intent(this, MainActivity.class);
    }

    @Override
    public Intent getMainLauncherIntent() {
        return new Intent(this, GatewaySplashActivity.class);
    }

    @Override
    public INetworkRequest getNetworkRequest() {
        return networkRequest;
    }
}