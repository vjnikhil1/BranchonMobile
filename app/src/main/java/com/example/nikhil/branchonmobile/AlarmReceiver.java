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
import android.support.v4.app.NotificationCompat;
import android.util.Log;

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
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher))
                .setSound(uri)
                .build();
        notification.defaults = Notification.DEFAULT_VIBRATE;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(), notification);
    }
}
