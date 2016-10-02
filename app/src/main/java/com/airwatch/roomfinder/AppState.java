/*
 * Copyright (c) 2016 AirWatch, LLC. All rights reserved.
 * This product is protected by copyright and intellectual property laws in
 * the United States and other countries as well as by international treaties.
 * AirWatch products may be covered by one or more patents listed at
 * http://www.vmware.com/go/patents.
 *
 */

package com.airwatch.roomfinder;

/**
 * Created by jmara on 8/11/2016.
 */
public class AppState {
    private State currentState;
    public static final String APP_STATE_KEY = "app.state.key";

    void setCurrentState(State state){
        currentState = state;
    }

    public State getCurrentState(){
        return currentState;
    }

    public enum State {
        URL_ENTRY,
        SHOW_LOCATION_LIST,
        SHOW_ROOM_LIST
    }
}