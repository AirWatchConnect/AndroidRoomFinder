/*
 * Copyright (c) 2016 AirWatch, LLC. All rights reserved.
 * This product is protected by copyright and intellectual property laws in
 * the United States and other countries as well as by international treaties.
 * AirWatch products may be covered by one or more patents listed at
 * http://www.vmware.com/go/patents.
 *
 */

package com.airwatch.roomfinder.roomslist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airwatch.roomfinder.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmara on 8/12/2016.
 */
public class RoomsListFragment extends Fragment {

    public static final String TAG = "RoomsListFragment";
    private RecyclerView recyclerView;
    private TextView statusText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_roomlist, container, false);
        initUI(rootView);
        return rootView;
    }

    private void initUI(View rootView){
        recyclerView = (RecyclerView) rootView.findViewById(R.id.roomlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        statusText = (TextView) rootView.findViewById(R.id.status_text);
        statusText.setVisibility(View.GONE);
        recyclerView.setAdapter(new RoomsListAdapter(getRoomList()));
        recyclerView.setVisibility(View.VISIBLE);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.rooms);
    }

    private List<RoomDetails> getRoomList(){
        return RoomAvailabilityXmlParser.getInstance().getRoomDetailsList();
    }
}
