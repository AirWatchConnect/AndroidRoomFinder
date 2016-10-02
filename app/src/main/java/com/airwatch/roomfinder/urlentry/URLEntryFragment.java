/*
 * Copyright (c) 2016 AirWatch, LLC. All rights reserved.
 * This product is protected by copyright and intellectual property laws in
 * the United States and other countries as well as by international treaties.
 * AirWatch products may be covered by one or more patents listed at
 * http://www.vmware.com/go/patents.
 *
 */

package com.airwatch.roomfinder.urlentry;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airwatch.roomfinder.AppState;
import com.airwatch.roomfinder.IAppStateHandler;
import com.airwatch.roomfinder.R;
import com.airwatch.roomfinder.locationlist.LocationListXmlParser;
import com.airwatch.roomfinder.network.INetworkRequest;
import com.airwatch.roomfinder.network.NetworkRequest;
import com.airwatch.roomfinder.network.NetworkRequestFactory;
import com.airwatch.roomfinder.utils.KeyboardUtils;
import com.airwatch.roomfinder.utils.NetworkStatus;
import com.airwatch.sdk.AirWatchSDKException;
import com.airwatch.sdk.SDKManager;

import java.net.MalformedURLException;

/**
 * Created by jmara on 8/11/2016.
 */
public class URLEntryFragment extends Fragment {

    public static final String TAG = "URLEntryFragment";
    private EditText urlEntry;
    private EditText usernameEntry;
    private EditText passwordEntry;
    private Button loginButton;
    private IAppStateHandler appStateHandler;
    private RelativeLayout rootLayout;
    private ProgressDialog progressDialog;
    private boolean isAuthenticating;

    private TextWatcher urlTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            enableUserCredentials(validateURL(s.toString()));
        }
    };

    private TextWatcher usernameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            enableLoginButton(s.length() > 0 && !TextUtils.isEmpty(passwordEntry.getText().toString()));
        }
    };

    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            enableLoginButton(s.length() > 0 && !TextUtils.isEmpty(usernameEntry.getText().toString()));
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (isAuthenticating){
            showAuthenticationDialog();
        }
    }

    private void enableUserCredentials(boolean shouldEnable){
        if (shouldEnable){
            usernameEntry.setVisibility(View.VISIBLE);
            usernameEntry.setEnabled(true);
            usernameEntry.addTextChangedListener(usernameTextWatcher);
            passwordEntry.setVisibility(View.VISIBLE);
            passwordEntry.setEnabled(true);
            passwordEntry.addTextChangedListener(passwordTextWatcher);
        } else {
            usernameEntry.setVisibility(View.GONE);
            usernameEntry.setEnabled(false);
            usernameEntry.setText("");
            usernameEntry.removeTextChangedListener(usernameTextWatcher);
            passwordEntry.setVisibility(View.GONE);
            passwordEntry.setEnabled(false);
            passwordEntry.setText("");
            passwordEntry.removeTextChangedListener(passwordTextWatcher);
            enableLoginButton(false);
        }
    }

    private boolean validateURL(String urlText){
        return Patterns.WEB_URL.matcher(urlText).matches();
    }

    private void enableLoginButton(boolean shouldEnable){
        loginButton.setEnabled(shouldEnable);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentRootView = inflater.inflate(R.layout.fragment_urlentry, container, false);
        setRetainInstance(true);
        getAppStateHandler();
        initUI(fragmentRootView);
        return fragmentRootView;
    }

    private void getAppStateHandler(){
        appStateHandler = (IAppStateHandler) getActivity();
    }

    private void initUI(View fragmentRootView){
        urlEntry = (EditText) fragmentRootView.findViewById(R.id.url_entry);
        urlEntry.addTextChangedListener(urlTextWatcher);
        loginButton = (Button) fragmentRootView.findViewById(R.id.login_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        rootLayout = (RelativeLayout) fragmentRootView.findViewById(R.id.url_entry_rootlayout);
        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideKeyboard(getActivity());
            }
        });
        usernameEntry = (EditText) fragmentRootView.findViewById(R.id.username);
        passwordEntry = (EditText) fragmentRootView.findViewById(R.id.password);
        passwordEntry.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    login();
                    return true;
                }
                return false;
            }
        });
        enableUserCredentials(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        getExchangeURLFromCustomSettings();
    }

    private void getExchangeURLFromCustomSettings(){
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... params) {
                String url = "";
                try {
                    SDKManager sdkManager = SDKManager.init(getActivity().getApplicationContext());
                    url = sdkManager.getCustomSettings();
                } catch (AirWatchSDKException e) {
                    e.printStackTrace();
                }

                return url;
            }

            @Override
            protected void onPostExecute(String s) {
                if (!TextUtils.isEmpty(s)){
                    urlEntry.setText(s);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void showAuthenticationDialog(){
        isAuthenticating = true;
        progressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.authenticating), true, false);
    }

    private void hideAuthenticationDialog(){
        isAuthenticating = false;
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

    private void login(){
        if (!NetworkStatus.isNetworkConnected(getContext())){
            showErrorDialog(R.string.internet_connection_not_available, R.string.internet_connection_not_available_msg);
        }
        showAuthenticationDialog();
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                boolean isAuthenticated = false;
                if (validateInputFields()) {
                    isAuthenticated = isUserAuthenticated();
                }
                return isAuthenticated;
            }

            @Override
            protected void onPostExecute(Boolean isAuthenticated) {
                hideAuthenticationDialog();
                KeyboardUtils.hideKeyboard(getActivity());
                if (URLEntryFragment.this.isVisible()) {
                    if (isAuthenticated) {
                        appStateHandler.updateState(AppState.State.SHOW_LOCATION_LIST);
                    } else {
                        showErrorDialog(R.string.login_failed, R.string.login_failed_msg);
                    }
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private boolean validateInputFields(){
        return !TextUtils.isEmpty(urlEntry.getText());// &&  !TextUtils.isEmpty(usernameEntry.getText()) && !TextUtils.isEmpty(passwordEntry.getText());
    }

    private boolean isUserAuthenticated(){
        boolean isAuthenticated = false;
        INetworkRequest networkRequest = ((NetworkRequestFactory)getActivity().getApplicationContext()).getNetworkRequest();
        networkRequest.setUserCredentials(usernameEntry.getText().toString().trim(), passwordEntry.getText().toString());
        try {
            networkRequest.setUrl(urlEntry.getText().toString().trim());
            String response = networkRequest.requestLocationList();
            isAuthenticated = !TextUtils.isEmpty(response);
            if (isAuthenticated){
                LocationListXmlParser.getInstance().parseXml(response);
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "Invalid URL", e);
            urlEntry.setError("Invalid URL");
            enableLoginButton(false);
            enableUserCredentials(false);
        }
        return isAuthenticated;
    }
}
