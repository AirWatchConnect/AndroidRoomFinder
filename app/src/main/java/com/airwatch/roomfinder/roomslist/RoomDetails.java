/*
 * Copyright (c) 2016 AirWatch, LLC. All rights reserved.
 * This product is protected by copyright and intellectual property laws in
 * the United States and other countries as well as by international treaties.
 * AirWatch products may be covered by one or more patents listed at
 * http://www.vmware.com/go/patents.
 *
 */

package com.airwatch.roomfinder.roomslist;

/**
 * Created by jmara on 8/12/2016.
 */
public class RoomDetails {

    private String roomName;
    private Availability roomAvailability;
    private String roomEmail;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Availability getRoomAvailability() {
        return roomAvailability;
    }

    public void setRoomAvailability(Availability roomAvailable) {
        roomAvailability = roomAvailable;
    }

    public String getRoomEmail() {
        return roomEmail;
    }

    public void setRoomEmail(String roomEmail) {
        this.roomEmail = roomEmail;
    }

    enum Availability{
        AVAILABLE("Available NOW!"), NOTAVAILABLE("Not Available!"), AVAILABLE_SOON("Available in "), UNKNOWN("Status Unknown");

        private String statusMsg;

        Availability(String statusMsg){
            this.statusMsg = statusMsg;
        }


        public String getStatusMsg() {
            return statusMsg;
        }

        public Availability setStatusMsg(String statusMsg) {
            this.statusMsg = statusMsg;
            return this;
        }
    }
}
