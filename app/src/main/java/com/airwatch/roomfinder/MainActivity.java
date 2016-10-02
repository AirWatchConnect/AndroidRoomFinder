/*
 * Copyright (c) 2016 AirWatch, LLC. All rights reserved.
 * This product is protected by copyright and intellectual property laws in
 * the United States and other countries as well as by international treaties.
 * AirWatch products may be covered by one or more patents listed at
 * http://www.vmware.com/go/patents.
 *
 */

package com.airwatch.roomfinder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.airwatch.roomfinder.locationlist.LocationListFragment;
import com.airwatch.roomfinder.roomslist.RoomsListFragment;
import com.airwatch.roomfinder.urlentry.URLEntryFragment;

/**
 * Created by jmara on 8/11/2016.
 */
public class MainActivity extends AppCompatActivity implements IAppStateHandler{

    private FragmentManager fragmentManager;
    private AppState appState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appState = new AppState();
        handleStateOnActivityRestore(savedInstanceState);
    }

    private void handleStateOnActivityRestore(Bundle savedInstanceState){
        if (savedInstanceState != null){
            AppState.State currentState = (AppState.State)savedInstanceState.get(AppState.APP_STATE_KEY);
            if (currentState != null){
                updateState(currentState);
                return;
            }
        }
        updateState(AppState.State.URL_ENTRY);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(AppState.APP_STATE_KEY, appState.getCurrentState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        switch (appState.getCurrentState()){
            case URL_ENTRY:
                finish();
                break;
            case SHOW_ROOM_LIST:
                fragmentManager.popBackStackImmediate();
                appState.setCurrentState(AppState.State.SHOW_LOCATION_LIST);
                break;
            case SHOW_LOCATION_LIST:
                fragmentManager.popBackStackImmediate();
                appState.setCurrentState(AppState.State.URL_ENTRY);
                break;
            default:
                super.onBackPressed();
                break;
        }
    }

    private void replaceFragment(String tag){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right);
        if (getFragmentFromBackStack(tag) != null){
            fragmentTransaction.replace(R.id.fragment_layout, getFragmentFromBackStack(tag), tag).commit();
        } else {
            fragmentTransaction.replace(R.id.fragment_layout, createFragment(tag), tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    private String getTagFromState(AppState.State state){
        switch (state){
            case URL_ENTRY:
                return URLEntryFragment.TAG;
            case SHOW_ROOM_LIST:
                return RoomsListFragment.TAG;
            case SHOW_LOCATION_LIST:
                return LocationListFragment.TAG;
            default:
                throw new Error("Unknown state encountered");
        }
    }

    private Fragment getFragmentFromBackStack(String tag){
        return fragmentManager.findFragmentByTag(tag);
    }

    private Fragment createFragment(String tag){
        Fragment fragment;
        switch (tag){
            case URLEntryFragment.TAG:
                fragment = new URLEntryFragment();
                break;
            case RoomsListFragment.TAG:
                fragment = new RoomsListFragment();
                break;
            case LocationListFragment.TAG:
                fragment = new LocationListFragment();
                break;
            default:
                throw new Error("Unknown state encountered");
        }
        return fragment;
    }

    @Override
    public void updateState(AppState.State state) {
        if (state == appState.getCurrentState()){
            return;                                 //Ignoring the update if the change is already made
        }
        appState.setCurrentState(state);
        replaceFragment(getTagFromState(state));
    }

    @Override
    public AppState getAppState() {
        return appState;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
