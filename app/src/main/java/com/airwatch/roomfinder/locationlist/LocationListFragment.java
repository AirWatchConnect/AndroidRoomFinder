/*
 * Copyright (c) 2016 AirWatch, LLC. All rights reserved.
 * This product is protected by copyright and intellectual property laws in
 * the United States and other countries as well as by international treaties.
 * AirWatch products may be covered by one or more patents listed at
 * http://www.vmware.com/go/patents.
 *
 */

package com.airwatch.roomfinder.locationlist;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airwatch.roomfinder.AppState;
import com.airwatch.roomfinder.IAppStateHandler;
import com.airwatch.roomfinder.R;
import com.airwatch.roomfinder.network.INetworkRequest;
import com.airwatch.roomfinder.network.NetworkRequest;
import com.airwatch.roomfinder.network.NetworkRequestFactory;
import com.airwatch.roomfinder.network.SoapBodyTemplate;
import com.airwatch.roomfinder.roomslist.RoomAvailabilityXmlParser;
import com.airwatch.roomfinder.roomslist.RoomDetails;
import com.airwatch.roomfinder.roomslist.RoomListXmlParser;
import com.airwatch.roomfinder.utils.NetworkStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by jmara on 8/15/2016.
 */
public class LocationListFragment extends Fragment {

    public static final String TAG = "LocationListFragment";
    private RecyclerView locationList;
    private IAppStateHandler appStateHandler;
    private ProgressDialog progressDialog;
    private List<LocationListModel> locationListData;
    private boolean isFetching;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_locationlist, container, false);
        setRetainInstance(true);
        getAppStateHandler();
        initUI(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFetching){
            showAuthenticationDialog();
        }
    }

    private void getAppStateHandler(){
        appStateHandler = (IAppStateHandler) getActivity();
    }

    private void initUI(View rootView) {
        locationList = (RecyclerView) rootView.findViewById(R.id.locationlist);
        locationList.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        locationList.setAdapter(new LocationListAdapter(getLocationList(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRoomList(((TextView)v).getText().toString());

            }
        }));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.location);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    private void showAuthenticationDialog(){
        isFetching = true;
        progressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.fetching_room_list), true, false);
    }

    private void hideAuthenticationDialog(){
        isFetching = false;
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    private void showErrorDialog(int title, int message){
        new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(message).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private List<LocationListModel> getLocationList(){
        return locationListData = LocationListXmlParser.getInstance().getLocationList();
    }

    private void requestRoomList(final String roomName){
        if (!NetworkStatus.isNetworkConnected(getContext())){
            showErrorDialog(R.string.internet_connection_not_available, R.string.internet_connection_not_available_msg);
        }
        showAuthenticationDialog();
        new AsyncTask<Void, Void, Boolean>(){
            @Override
            protected Boolean doInBackground(Void... params) {
                INetworkRequest networkRequest = ((NetworkRequestFactory)getActivity().getApplicationContext()).getNetworkRequest();
                String response = networkRequest.requestRoomList(getRoomEmail(roomName));
                Boolean isSuccess = !TextUtils.isEmpty(response);
                if (isSuccess){
                    RoomListXmlParser parser = RoomListXmlParser.getInstance();
                    parser.parseXml(response);
                    isSuccess = requestRoomAvailability();
                }
                return isSuccess;
            }

            @Override
            protected void onPostExecute(Boolean flag) {
                if (flag) {
                    appStateHandler.updateState(AppState.State.SHOW_ROOM_LIST);
                } else {
                    showErrorDialog(R.string.something_went_wrong, R.string.error_fetching_rooms);
                }
                hideAuthenticationDialog();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private boolean requestRoomAvailability(){
        INetworkRequest networkRequest = ((NetworkRequestFactory)getActivity().getApplicationContext()).getNetworkRequest();
        String response = networkRequest.requestRoomAvailability(prepareXmlString());
        boolean isSuccess = !TextUtils.isEmpty(response);
        if (isSuccess){
            RoomAvailabilityXmlParser parser = RoomAvailabilityXmlParser.getInstance();
            parser.parse(response);
        }
        return isSuccess;
    }

    private String getRoomEmail(String roomName){
        String roomEmail = "";
        for (LocationListModel locationListModel : locationListData){
            if (locationListModel.getName().equals(roomName)){
                roomEmail = locationListModel.getEmail();
                break;
            }
        }
        return roomEmail;
    }

    private String prepareXmlString(){
        String mailboxArrayString = "";
        for (RoomDetails roomDetail : RoomListXmlParser.getInstance().getRoomList()){
            mailboxArrayString = mailboxArrayString + String.format(SoapBodyTemplate.mailBoxArrayTemplate, roomDetail.getRoomName(), roomDetail.getRoomEmail());
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String startingTime = formatter.format(getPreviousTimeslot(new Date()));
        String endingTime = formatter.format(getPreviousTimeslot(new Date(System.currentTimeMillis() + 90 * 60 * 1000)));
        return String.format(SoapBodyTemplate.roomAvailabilitySoapBody, mailboxArrayString, startingTime, endingTime);
    }

    private Date getPreviousTimeslot(Date date){
        if (date.getMinutes() >= 30){
            date.setMinutes(30);
        } else {
            date.setMinutes(0);
        }
        return date;
    }
}
