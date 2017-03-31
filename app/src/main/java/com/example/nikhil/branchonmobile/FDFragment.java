package com.example.nikhil.branchonmobile;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class FDFragment extends Fragment {
    private EditText time;
    private Button sub;


    public FDFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fd, container, false);
        time = (EditText) view.findViewById(R.id.editText16);
        sub = (Button) view.findViewById(R.id.button7);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
                Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
                notificationIntent.addCategory("android.intent.category.DEFAULT");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, Integer.parseInt(time.getText().toString()));
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        });
        return view;
    }

}
