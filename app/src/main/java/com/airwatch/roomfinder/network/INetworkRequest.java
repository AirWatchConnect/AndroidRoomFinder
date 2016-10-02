package com.airwatch.roomfinder.network;

import java.net.MalformedURLException;

public interface INetworkRequest {

    void setUserCredentials(String username, String password);
    void setUrl(String url) throws MalformedURLException;
    String requestRoomList(String roomEmail);
    String requestLocationList();
    String requestRoomAvailability(String postData);
}