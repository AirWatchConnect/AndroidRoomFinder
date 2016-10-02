package com.airwatch.roomfinder.locationlist;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created by jmara on 8/18/2016.
 */
public class LocationListXmlHandler extends DefaultHandler {

    private ArrayList<LocationListModel> locationList = new ArrayList<>();
    private LocationListModel locationData;
    private boolean isLocationName, isLocationAddress;

    public ArrayList<LocationListModel> getLocationList(){
        return locationList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (localName){
            case "Address":
                locationData = new LocationListModel();
                break;
            case "Name":
                isLocationName = true;
                break;
            case "EmailAddress":
                isLocationAddress = true;
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (localName){
            case "Address":
                locationList.add(locationData);
                break;
            case "Name":
                isLocationName = false;
                break;
            case "EmailAddress":
                isLocationAddress = false;
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isLocationName){
            locationData.setName(new String(ch, start, length));
        } else if (isLocationAddress){
            locationData.setEmail(new String(ch, start, length));
        }
    }
}
