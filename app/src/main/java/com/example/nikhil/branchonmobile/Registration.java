package com.example.nikhil.branchonmobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Registration extends AppCompatActivity {
    private Button register;
    private RadioGroup rg;
    private RadioButton rb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        register = (Button) findViewById(R.id.button2);
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected = rg.getCheckedRadioButtonId();
                rb = (RadioButton) findViewById(selected);
                Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
