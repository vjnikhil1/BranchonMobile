package com.example.nikhil.branchonmobile;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.net.ConnectException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * Created by Nikhil on 31-03-2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    DBHelper db;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Broadcast","Reached");
        db = new DBHelper(context);
        boolean v = db.insertData(intent.getStringExtra("period"),intent.getStringExtra("amount"));
        if(v==true)
            Log.e("SQLite", "Inserted");
//        Log.e("SQLite Result", buffer.toString());
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notification = builder.setContentTitle("Fixed Deposit Status")
                .setTicker("Your Fixed Deposit Time has lapsed")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Your Fixed Deposit Time has lapsed"))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_menu_send)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher_no_background))
                .setSound(uri)
                .build();
        notification.defaults = Notification.DEFAULT_VIBRATE;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(), notification);
        CheckInternetAsync ca = new CheckInternetAsync(context);
        ca.execute();
    }
    class CheckInternetAsync extends AsyncTask<String, Void, Boolean> {

        private Context context;

        CheckInternetAsync(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return isInternetAvailable();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                DBHelper db = new DBHelper(context);
                Cursor res = db.getData();
                while (res.moveToNext()) {
                    Log.e("Cloud Push", res.getString(0) + res.getString(1));
                    FingerprintHandler fh = new FingerprintHandler(context);
                    FingerprintHandler.TransferAsync ta = fh.new TransferAsync(context, "FD");
                    ta.execute("FD", ((Integer.parseInt(res.getString(1))) +
                            (Integer.parseInt(res.getString(0)) * (Integer.parseInt(res.getString(1)) / 100))) + "", "FD");
                }
                db.dropTable();
            }
        }

        public boolean isInternetAvailable() {
            try {
                final InetAddress address = InetAddress.getByName("www.google.com");
                return !address.equals("");
            } catch (UnknownHostException e) {
                // Log error
            }
            return false;
        }
    }
}
