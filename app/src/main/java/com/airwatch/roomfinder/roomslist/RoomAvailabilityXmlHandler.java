package com.airwatch.roomfinder.roomslist;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by jmara on 8/18/2016.
 */
public class RoomAvailabilityXmlHandler extends DefaultHandler {

    private List<RoomDetails> roomDetailsList = RoomListXmlParser.getInstance().getRoomList();
    private RoomDetails roomDetails;
    private boolean isRoomStatus;
    private boolean isStartTime;
    private int i=0;

    public List<RoomDetails> getRoomDetailsList(){
        return roomDetailsList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (localName){
            case "FreeBusyResponse":
                roomDetails = roomDetailsList.get(i++);
                break;
            case "MergedFreeBusy":
                isRoomStatus = true;
                break;
            case "StartTime":
                isStartTime = true;
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (localName){
            case "MergedFreeBusy":
                isRoomStatus = false;
                break;
            case "StartTime":
                isStartTime = false;
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isRoomStatus && "000".equals(new String(ch, start, length))){
            roomDetails.setRoomAvailability(RoomDetails.Availability.AVAILABLE);
        } else if (isStartTime){
            try {
                String diffString = getTimeDifference(new String(ch, start, length));
                if (diffString == null){
                    roomDetails.setRoomAvailability(RoomDetails.Availability.NOTAVAILABLE);
                } else {
                    roomDetails.setRoomAvailability(RoomDetails.Availability.AVAILABLE_SOON.setStatusMsg(diffString));
                }
            } catch (ParseException e) {
                e.printStackTrace();
                roomDetails.setRoomAvailability(RoomDetails.Availability.UNKNOWN);
            }
        }
    }

    private String getTimeDifference(String roomFreeTime) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date nextAvailableTime = simpleDateFormat.parse(roomFreeTime);
        int diff = (int)TimeUnit.MINUTES.convert(nextAvailableTime.getTime() - new Date().getTime(), TimeUnit.MILLISECONDS);
        String diffString = "Available in ";
        if (diff > 0) {
            if (diff >= 60) {
                diffString = diffString + (diff % 60) + " Hours ";
                diff /= 60;
            }
            diffString = diffString + diff + " Minutes!!!";
        } else {
            diffString = null;
        }
        return diffString;
    }
}
