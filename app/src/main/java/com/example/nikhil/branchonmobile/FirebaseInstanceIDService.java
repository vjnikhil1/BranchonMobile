package com.example.nikhil.branchonmobile;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Nikhil on 26-02-2017.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    SharedPreferences pref;
    String result;
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        pref = getSharedPreferences("BOM", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token", token);
        editor.commit();
        Log.e("token",token);
    }
}
