/*
 * Copyright (c) 2016 AirWatch, LLC. All rights reserved.
 * This product is protected by copyright and intellectual property laws in
 * the United States and other countries as well as by international treaties.
 * AirWatch products may be covered by one or more patents listed at
 * http://www.vmware.com/go/patents.
 *
 */

package com.airwatch.roomfinder.locationlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airwatch.roomfinder.R;

import java.util.List;

/**
 * Created by jmara on 8/15/2016.
 */
public class LocationListAdapter extends RecyclerView.Adapter<LocationListViewHolder> {

    private List<LocationListModel> locationList;
    private View.OnClickListener locationListClickListener;

    public LocationListAdapter(List<LocationListModel> locationList, View.OnClickListener locationListClickListener){
        this.locationList = locationList;
        this.locationListClickListener = locationListClickListener;
    }

    @Override
    public LocationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list_view, null);
        return new LocationListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationListViewHolder holder, int position) {
        String locationName = locationList.get(position).getName();
        holder.locationlistitem.setText(locationName);
        holder.locationlistitem.setOnClickListener(locationListClickListener);
    }

    @Override
    public int getItemCount() {
        return (locationList.size()>0)?locationList.size():0;
    }
}

class LocationListViewHolder extends RecyclerView.ViewHolder {

    protected TextView locationlistitem;

    public LocationListViewHolder(View itemView) {
        super(itemView);
        locationlistitem = (TextView) itemView.findViewById(R.id.locationlistitem);
    }
}
