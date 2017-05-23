package com.example.nikhil.branchonmobile;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForgotActivity extends AppCompatActivity {
    private EditText accNo, dob, email, pass, pass1;
    private Button generate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        accNo = (EditText) findViewById(R.id.accno);
        dob = (EditText) findViewById(R.id.editText13);
        email = (EditText) findViewById(R.id.editText14);
        pass = (EditText) findViewById(R.id.editText15);
        pass1 = (EditText) findViewById(R.id.password);
        generate = (Button) findViewById(R.id.buttonDD);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<EditText> ver = Arrays.asList(accNo,dob,email,pass,pass1);
                int flag = 1;

                if(!pass.getText().toString().equals(pass1.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
                    flag=0;
                }
                for(int i=0;i<ver.size();i++){
                    if(ver.get(i).getText().toString().trim().equals("")){
                        Log.e("mand", i+"");
                        ver.get(i).setError("Field is mandatory");
                        flag = 0;
                    }
                    else if(ver.get(i).getText().toString().trim().length()>0) {
                        if(i==2) {
                            if(!ver.get(i).getText().toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                                ver.get(i).setError("Email must be of the format xxx@xxx.com");
                                flag = 0;
                            }
                        }
                        if(i==3||i==4) {
                            if(!ver.get(i).getText().toString().trim().matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+-=;:',./<>?]).{8,20})")) {
                                Log.e("password", ver.get(i).getText().toString().trim());
                                ver.get(i).setError("Not a valid password. Must contain atleast\n" +
                                        "-One number\n" +
                                        "-One uppercase letter\n" +
                                        "-One special character\n" +
                                        "-Length >= 8 characters");
                                flag = 0;
                            }
                        }
                    }
                }
                if(flag==1){
                    ForgotTask ft = new ForgotTask(ForgotActivity.this);
                    ft.execute(dob.getText().toString(), email.getText().toString(), pass.getText().toString()
                            , accNo.getText().toString());
                }
            }
        });
    }
    public class ForgotTask extends AsyncTask<String,Void,String> {
        Activity c;
        ProgressDialog dialog;
        String route;
        String result;
        RecyclerView recyclerView;
        FDAdapter adapter;
        Fragment a;
        ForgotTask(Activity c){
            this.c = c;
            dialog = new ProgressDialog(c);
        }
        ForgotTask(Fragment a){
            this.a = a;
            dialog = new ProgressDialog(a.getActivity());
        }
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please Wait");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url_getdetails = "http://52.33.154.120:8080/forgotpassword.php";
            String dob = params[0];
            String email = params[1];
            String pass = params[2];
            String accNo = params[3];
            try {
                URL url = new URL(url_getdetails);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                OutputStream os = con.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String post = URLEncoder.encode("dob", "UTF-8") + "=" + URLEncoder.encode(dob, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8") + "&" +
                        URLEncoder.encode("accNo", "UTF-8") + "=" + URLEncoder.encode(accNo, "UTF-8");
                bufferedWriter.write(post);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream is = con.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                is.close();
                con.disconnect();
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("Password reset", s);
            if(dialog.isShowing()){
                dialog.dismiss();
            }
            if(s.equals("1")) {
                Toast.makeText(c, "Please wait for password reset mail", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(c, "Please check your credentials", Toast.LENGTH_LONG).show();
            }
        }
    }
}
