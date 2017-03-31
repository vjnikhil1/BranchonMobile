package com.example.nikhil.branchonmobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView t1;
    private Button login;
    private EditText username,pass;
    private SharedPreferences pref;
    private BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getSharedPreferences("BOM", 0);
        t1 = (TextView) findViewById(R.id.textView3);
        username = (EditText) findViewById(R.id.editText);
        if(!pref.getString("accNo","0").equals("0"))
            username.setText(pref.getString("accNo",null));
        pass = (EditText) findViewById(R.id.editText3);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, Registration.class);
                startActivity(intent);
            }
        });
        login = (Button) findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParallelThread p = new ParallelThread(MainActivity.this);
                p.execute("login",username.getText().toString(),pass.getText().toString());
            }
        });
    }
}
