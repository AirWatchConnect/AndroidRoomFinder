/*
 * Copyright (c) 2016 AirWatch, LLC. All rights reserved.
 * This product is protected by copyright and intellectual property laws in
 * the United States and other countries as well as by international treaties.
 * AirWatch products may be covered by one or more patents listed at
 * http://www.vmware.com/go/patents.
 *
 */

package com.airwatch.roomfinder.roomslist;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.airwatch.roomfinder.R;

import java.util.List;

/**
 * Created by jmara on 8/12/2016.
 */
public class RoomsListAdapter extends RecyclerView.Adapter<RoomListViewHolder> {
    private List<RoomDetails> roomDetailsList;

    public RoomsListAdapter(List<RoomDetails> roomDetailsList){
        this.roomDetailsList = roomDetailsList;
    }

    @Override
    public RoomListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_list_view, null);
        return new RoomListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomListViewHolder holder, int position) {
        RoomDetails roomDetails = roomDetailsList.get(position);
        holder.roomName.setText(roomDetails.getRoomName());
        holder.roomStatus.setText(roomDetails.getRoomAvailability().getStatusMsg());
        Drawable drawable = holder.roomStatusSymbol.getBackground();
        int color = 0;
        switch (roomDetails.getRoomAvailability()){
            case AVAILABLE:
                color = Color.parseColor("#7FB447");
                break;
            case NOTAVAILABLE:
                color = Color.RED;
                break;
            case AVAILABLE_SOON:
                color = Color.parseColor("#FF7F55");
                break;
            case UNKNOWN:
                color = Color.GRAY;
                break;
        }
        drawable.setColorFilter(color, PorterDuff.Mode.OVERLAY);
    }

    @Override
    public int getItemCount() {
        return (roomDetailsList!=null)?roomDetailsList.size():0;
    }
}

class RoomListViewHolder extends RecyclerView.ViewHolder {
    protected FrameLayout roomStatusSymbol;
    protected TextView roomName;
    protected TextView roomStatus;

    public RoomListViewHolder(View itemView) {
        super(itemView);
        roomName = (TextView)itemView.findViewById(R.id.room_name);
        roomStatus = (TextView) itemView.findViewById(R.id.room_status);
        roomStatusSymbol = (FrameLayout) itemView.findViewById(R.id.room_status_symbol);
    }
}
