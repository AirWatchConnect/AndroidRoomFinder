package com.airwatch.roomfinder.network;

/**
 * Created by jmara on 8/18/2016.
 */
public interface SoapBodyTemplate {

    String locationListSoapBody = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
            "               xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
            "               xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\"\n" +
            "               xmlns:m=\"http://schemas.microsoft.com/exchange/services/2006/messages\">\n" +
            "  <soap:Header>\n" +
            "    <t:RequestServerVersion Version =\"Exchange2010_SP1\"/>\n" +
            "  </soap:Header>\n" +
            "  <soap:Body>\n" +
            "    <m:GetRoomLists />\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";

    String roomListSoapBody = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
            "               xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
            "               xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\"\n" +
            "               xmlns:m=\"http://schemas.microsoft.com/exchange/services/2006/messages\">\n" +
            "  <soap:Header>\n" +
            "    <t:RequestServerVersion Version =\"Exchange2010_SP1\"/>\n" +
            "  </soap:Header>\n" +
            "  <soap:Body>\n" +
            "    <m:GetRooms>\n" +
            "      <m:RoomList>\n" +
            "        <t:EmailAddress>%s</t:EmailAddress>\n" +
            "      </m:RoomList>\n" +
            "    </m:GetRooms>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";

    String roomAvailabilitySoapBody = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" +
            "               xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" \n" +
            "               xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <GetUserAvailabilityRequest xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\">\n" +
            "      <TimeZone xmlns=\"http://schemas.microsoft.com/exchange/services/2006/types\">\n" +
            "        <Bias>300</Bias>\n" +
            "        <StandardTime>\n" +
            "          <Bias>0</Bias>\n" +
            "          <Time>02:00:00</Time>\n" +
            "          <DayOrder>5</DayOrder>\n" +
            "          <Month>10</Month>\n" +
            "          <DayOfWeek>Sunday</DayOfWeek>\n" +
            "        </StandardTime>\n" +
            "        <DaylightTime>\n" +
            "          <Bias>-60</Bias>\n" +
            "          <Time>02:00:00</Time>\n" +
            "          <DayOrder>1</DayOrder>\n" +
            "          <Month>4</Month>\n" +
            "          <DayOfWeek>Sunday</DayOfWeek>\n" +
            "        </DaylightTime>\n" +
            "      </TimeZone>\n" +
            "      <MailboxDataArray>\n" +
            "        %s\n" +
            "      </MailboxDataArray>\n" +
            "      <FreeBusyViewOptions xmlns=\"http://schemas.microsoft.com/exchange/services/2006/types\">\n" +
            "        <TimeWindow>\n" +
            "          <StartTime>%s</StartTime>\n" +
            "          <EndTime>%s</EndTime>\n" +
            "        </TimeWindow>\n" +
            "        <MergedFreeBusyIntervalInMinutes>30</MergedFreeBusyIntervalInMinutes>\n" +
            "        <RequestedView>FreeBusyMerged</RequestedView>\n" +
            "      </FreeBusyViewOptions>\n" +
            "    </GetUserAvailabilityRequest>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";

    String mailBoxArrayTemplate = "<MailboxData xmlns=\"http://schemas.microsoft.com/exchange/services/2006/types\">\n" +
            "          <Email>\n" +
            "            <Name>%s</Name>\n" +
            "            <Address>%s</Address>\n" +
            "            <RoutingType>SMTP</RoutingType>\n" +
            "          </Email>\n" +
            "          <AttendeeType>Organizer</AttendeeType>\n" +
            "          <ExcludeConflicts>false</ExcludeConflicts>     \n" +
            "        </MailboxData>";
}
