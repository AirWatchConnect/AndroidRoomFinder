package com.airwatch.roomfinder;

import android.app.Application;

import com.airwatch.roomfinder.network.INetworkRequest;
import com.airwatch.roomfinder.network.NetworkRequest;
import com.airwatch.roomfinder.network.NetworkRequestFactory;


public class RoomFinderApp extends Application implements NetworkRequestFactory {

    private INetworkRequest networkRequest = new NetworkRequest();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public INetworkRequest getNetworkRequest() {
        return networkRequest;
    }
}