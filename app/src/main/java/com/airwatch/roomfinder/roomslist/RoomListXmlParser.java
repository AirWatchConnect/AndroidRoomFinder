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
public class RoomListXmlParser {

    private static RoomListXmlParser roomListXmlParser;
    private List<RoomDetails> roomList;

    private RoomListXmlParser(){
    }

    public static synchronized RoomListXmlParser getInstance(){
        if (roomListXmlParser == null){
            roomListXmlParser = new RoomListXmlParser();
        }
        return roomListXmlParser;
    }

    public void parseXml(String xml){
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();

            RoomListXmlHandler roomListXmlHandler = new RoomListXmlHandler();
            xmlReader.setContentHandler(roomListXmlHandler);

            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(xml));
            xmlReader.parse(inputSource);

            roomList = roomListXmlHandler.getRoomDetailsList();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<RoomDetails> getRoomList(){
        return roomList;
    }
}
