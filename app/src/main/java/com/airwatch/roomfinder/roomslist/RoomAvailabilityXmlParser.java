package com.airwatch.roomfinder.roomslist;

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
public class RoomAvailabilityXmlParser {

    public static final String TAG = "RoomAvailabilityXmlParser";
    private List<RoomDetails> roomDetailsList;
    private static RoomAvailabilityXmlParser roomAvailabilityXmlParser;

    private RoomAvailabilityXmlParser(){}

    public static synchronized RoomAvailabilityXmlParser getInstance(){
        if (roomAvailabilityXmlParser == null){
            roomAvailabilityXmlParser = new RoomAvailabilityXmlParser();
        }
        return roomAvailabilityXmlParser;
    }

    public void parse(String xml){
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();

            RoomAvailabilityXmlHandler roomAvailabilityXmlHandler = new RoomAvailabilityXmlHandler();
            xmlReader.setContentHandler(roomAvailabilityXmlHandler);

            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(xml));
            xmlReader.parse(inputSource);

            roomDetailsList = roomAvailabilityXmlHandler.getRoomDetailsList();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<RoomDetails> getRoomDetailsList() {
        return roomDetailsList;
    }
}
