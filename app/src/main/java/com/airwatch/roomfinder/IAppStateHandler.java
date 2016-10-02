/*
 * Copyright (c) 2016 AirWatch, LLC. All rights reserved.
 * This product is protected by copyright and intellectual property laws in
 * the United States and other countries as well as by international treaties.
 * AirWatch products may be covered by one or more patents listed at
 * http://www.vmware.com/go/patents.
 *
 */

package com.airwatch.roomfinder;

import java.io.Serializable;

/**
 * Created by jmara on 8/11/2016.
 */
public interface IAppStateHandler extends Serializable {
    String APP_STATE_HANDLER_KEY = "app_state_handler";

    void updateState(AppState.State state);

    AppState getAppState();
}
