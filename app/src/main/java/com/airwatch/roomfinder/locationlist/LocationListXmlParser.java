package com.airwatch.roomfinder.locationlist;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by jmara on 8/18/2016.
 */
public class LocationListXmlParser {

    private static LocationListXmlParser locationListXmlParser;
    private List<LocationListModel> locationList;

    private LocationListXmlParser(){
    }

    public static synchronized LocationListXmlParser getInstance(){
        if (locationListXmlParser == null){
            locationListXmlParser = new LocationListXmlParser();
        }
        return locationListXmlParser;
    }

    public void parseXml(String xml){
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();

            LocationListXmlHandler locationListXmlHandler = new LocationListXmlHandler();
            xmlReader.setContentHandler(locationListXmlHandler);

            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(xml));
            xmlReader.parse(inputSource);

            locationList = locationListXmlHandler.getLocationList();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<LocationListModel> getLocationList(){
        return locationList;
    }
}
