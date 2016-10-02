package com.airwatch.roomfinder.roomslist;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmara on 8/18/2016.
 */
public class RoomListXmlHandler extends DefaultHandler {

    private List<RoomDetails> roomDetailsList = new ArrayList<>();
    private boolean isRoomName;
    private boolean isRoomEmail;
    RoomDetails roomDetail;

    public List<RoomDetails> getRoomDetailsList() {
        return roomDetailsList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (localName){
            case "Room":
                roomDetail = new RoomDetails();
                break;
            case "Name":
                isRoomName = true;
                break;
            case "EmailAddress":
                isRoomEmail = true;
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (localName){
            case "Room":
                roomDetailsList.add(roomDetail);
                break;
            case "Name":
                isRoomName = false;
                break;
            case "EmailAddress":
                isRoomEmail = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isRoomName){
            roomDetail.setRoomName(new String(ch, start, length));
        } else if (isRoomEmail){
            roomDetail.setRoomEmail(new String(ch, start, length));
        }
    }
}
