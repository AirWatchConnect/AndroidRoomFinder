package com.airwatch.roomfinder.network;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.airwatch.roomfinder.locationlist.LocationListModel;
import com.airwatch.roomfinder.roomslist.RoomDetails;
import com.airwatch.roomfinder.roomslist.RoomListXmlParser;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by jmara on 8/16/2016.
 */
public class NetworkRequest implements INetworkRequest{

    public static final String TAG = "NetworkRequest";
    private final String AUTH_TYPE = "ntlm";
    private final String DEFAULT_PROTOCOL = "https://";
    private final String PATH = "/EWS/Exchange.asmx";
    private String username;
    private String password;
    private URL url;

    @Override
    public void setUserCredentials(String username, String password){
        this.username = username;
        this.password = password;
    }

    @Override
    public void setUrl(String url) throws MalformedURLException {
        if (!url.startsWith("http")) {
            url = DEFAULT_PROTOCOL + url;
        }
        url = url + PATH;
        this.url = new URL(url);
    }

    private String makeRequest(HTTPMethod method, String postData) throws IOException {
        if (!validateParams()){
            Log.e(TAG, "Parameters not valid");
            return null;
        }

        String responseBody = "";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getAuthSchemes().register(AUTH_TYPE, new NTLMSchemeFactory());
        httpClient.getCredentialsProvider().setCredentials(new AuthScope(url.getHost(), -1), new NTCredentials(username, password, "", ""));
        HttpUriRequest httpUriRequest = getHttpUriRequest(method);
        httpUriRequest.setHeader(HTTP.CONTENT_TYPE, "text/xml");
        httpUriRequest.setHeader("Accept", "*/*");
        httpClient.getParams().setParameter("User-Agent", "RoomFinder/1 CFNetwork/711.3.18 Darwin/14.0.0");
        if (!TextUtils.isEmpty(postData) && method == HTTPMethod.POST){
            StringEntity stringEntity = new StringEntity(postData);
            ((HttpPost) httpUriRequest).setEntity(stringEntity);
        }
        HttpResponse response = httpClient.execute(httpUriRequest);
        Log.d(TAG, "Response code: " + response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            responseBody = EntityUtils.toString(response.getEntity());
            Log.d(TAG, "responseBody =>>>>>>>>>>" + responseBody);
        }
        return responseBody;
    }

    private HttpUriRequest getHttpUriRequest(HTTPMethod method){
        HttpUriRequest httpUriRequest = null;
        if (method == HTTPMethod.GET){
            httpUriRequest = new HttpGet(url.toExternalForm());
        } else if (method == HTTPMethod.POST){
            httpUriRequest = new HttpPost(url.toExternalForm());
        } else {
            throw new Error("Unsupported method found");
        }

        return httpUriRequest;
    }

    @Override
    public String requestRoomList(String roomEmail){
        String responseBody = "";
        try {
            responseBody = makeRequest(HTTPMethod.POST, String.format(SoapBodyTemplate.roomListSoapBody, roomEmail));
        } catch (IOException e){
            Log.e(TAG, "Error fetch room list", e);
        }
        return responseBody;
    }

    @Override
    public String requestLocationList(){
        String responseBody = "";
        try {
            responseBody = makeRequest(HTTPMethod.POST, SoapBodyTemplate.locationListSoapBody);
        } catch (IOException e){
            Log.e(TAG, "Error fetching location list", e);
        }
        return responseBody;
    }

    @Override
    public String requestRoomAvailability(String postData){
        String responseBody = "";
        try{
            responseBody = makeRequest(HTTPMethod.POST, postData);
        } catch (IOException e){
            Log.e(TAG, "Error fetching room availability", e);
        }
        return responseBody;
    }

    private boolean validateParams(){
        boolean isUrlValid = Patterns.WEB_URL.matcher(url.toExternalForm()).matches();
        return isUrlValid && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password);
    }

    enum HTTPMethod{
        GET, POST
    }
}
