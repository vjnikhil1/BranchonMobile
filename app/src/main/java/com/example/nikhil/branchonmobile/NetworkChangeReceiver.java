package com.example.nikhil.branchonmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Nikhil on 02-04-2017.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

        private static final String LOG_TAG = "NetworkChangeReceiver";
        private boolean isConnected = false;
        ProgressDialog pd;
        NetworkChangeReceiver(Context c){
            pd = new ProgressDialog(c);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(LOG_TAG, "Receieved notification about network status");
            isNetworkAvailable(context);
        }

        private boolean isNetworkAvailable(final Context context) {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            if (!isConnected) {
                                Log.v(LOG_TAG, "Now you are connected to Internet!");
                                Toast.makeText(context, "Internet availablle via Broadcast receiver", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                                isConnected = true;
                                // do your processing here ---
                                // if you need to post any data to the server or get
                                // status
                                // update from the server
                            }
                            return true;
                        }
                    }
                }
            }
            Log.v(LOG_TAG, "You are not connected to Internet!");
            pd.setMessage("Waiting for Internet Connection");
            pd.setCancelable(false);
            pd.show();
            isConnected = false;
            return false;
        }
}
