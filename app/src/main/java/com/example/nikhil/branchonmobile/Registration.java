package com.example.nikhil.branchonmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Registration extends AppCompatActivity {
    private Button register;
    private RadioGroup rg;
    private RadioButton rb;
    private EditText name,lname,email,mobile,pan,address,acctype;
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
                //Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_LONG).show();
                name = (EditText) findViewById(R.id.editText5);
                lname = (EditText) findViewById(R.id.editText6);
                email = (EditText) findViewById(R.id.editText7);
                mobile = (EditText) findViewById(R.id.editText8);
                address = (EditText) findViewById(R.id.editText10);
                ParallelThread pt = new ParallelThread(Registration.this);
                pt.execute("register",name.getText().toString(),lname.getText().toString(),
                        email.getText().toString(),mobile.getText().toString(),rb.getText().toString(),
                        "",address.getText().toString());
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
