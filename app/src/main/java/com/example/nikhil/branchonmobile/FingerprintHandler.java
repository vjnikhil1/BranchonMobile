package com.example.nikhil.branchonmobile;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.AsyncTask;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
 * Created by Nikhil on 06-02-2017.
 */

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private Context context;
    private SharedPreferences pref;
    SharedPreferences.Editor editor;


    // Constructor
    public FingerprintHandler(Context mContext) {
        context = mContext;
        pref = context.getSharedPreferences("BOM", 0);
        editor = pref.edit();
    }


    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if(cryptoObject==null&&context==null){
            success();
        }
        else {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        }
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint Authentication error\n" + errString, false);
    }


    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString, false);
    }


    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.", false);
    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Fingerprint Authentication succeeded.", true);
        success();
    }

    public void success(){
        if(pref.getString("printLoc", null).equals("login")) {
            editor.putString("printLoc", null);
            Intent intent = new Intent(context, HomeActivity.class);
            context.startActivity(intent);
        }
        else if(pref.getString("printLoc", null).equals("transfer")){
            editor.putString("printLoc", null);
            TransferAsync ta = new TransferAsync(context);
            ta.execute(pref.getString("rec", null), pref.getString("amount", null));
            Intent intent = new Intent(context, HomeActivity.class);
            context.startActivity(intent);
        }
        else if(pref.getString("printLoc", null).equals("update")){
            editor.putString("printLoc", null);
            UpdateAsync ua = new UpdateAsync(context);
            ua.execute("updateDetails", pref.getString("fname", null),
                    pref.getString("lname", null),
                    pref.getString("dob", null),
                    pref.getString("email", null),
                    pref.getString("phno", null),
                    pref.getString("address", null),
                    pref.getString("password", null));
            Intent intent = new Intent(context, HomeActivity.class);
            context.startActivity(intent);
        }
        else if(pref.getString("printLoc", null).equals("close")){
            editor.putString("printLoc", null);
            CloseAsync ca = new CloseAsync(context);
            ca.execute(pref.getString("accNoIn", null), pref.getString("reason", null));
        }
    }

    public void update(String e, Boolean success){
        TextView textView = (TextView) ((Activity)context).findViewById(R.id.errorText);
        textView.setText(e);
        if(success){
            textView.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        }
    }
    class TransferAsync extends AsyncTask<String,Void,String> {
        Context c;
        ProgressDialog dialog;
        String result;
        TransferAsync(Context c){
            this.c = c;
            dialog = new ProgressDialog(c);
        }
        @Override
        protected String doInBackground(String... params) {
            String url_transfer = "http://52.33.154.120:8080/transfer.php";//"http://bom.pe.hu/transfer.php";
            String receiver = params[0];
            String amount = params[1];
            SharedPreferences pref = c.getSharedPreferences("BOM", 0);
            String accNo = pref.getString("accNo", null);
            try {
                URL url = new URL(url_transfer);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                OutputStream os = con.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String post = URLEncoder.encode("accNo", "UTF-8") + "=" + URLEncoder.encode(accNo, "UTF-8") + "&" +
                        URLEncoder.encode("amount", "UTF-8") + "=" + URLEncoder.encode(amount, "UTF-8") + "&" +
                        URLEncoder.encode("receiver", "UTF-8") + "=" + URLEncoder.encode(receiver, "UTF-8");
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
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please Wait");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            if(dialog.isShowing())
                dialog.dismiss();
            Toast.makeText(context,s,Toast.LENGTH_LONG).show();
            Log.e("transfer", s);
        }
    }
    class UpdateAsync extends AsyncTask<String, Void, String>{
        Context c;
        ProgressDialog pd;
        SharedPreferences pref;
        String route;
        UpdateAsync(Context c){
            this.c = c;
            pd = new ProgressDialog(c);
            pref = c.getSharedPreferences("BOM", 0);
        }

        @Override
        protected void onPreExecute() {
            pd.setMessage("Please Wait");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            route = params[0];
            if(route.equals("updateDetails")){
                String fname = params[1];
                String lname = params[2];
                String dob = params[3];
                String email = params[4];
                String phno = params[5];
                String address = params[6];
                String password = params[7];
                String url_updatedetails = "http://52.33.154.120:8080/updatedetails.php";//"http://bom.pe.hu/updatedetails.php";
                try {
                    URL url = new URL(url_updatedetails);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream os = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    String post = URLEncoder.encode("accNo", "UTF-8") + "=" + URLEncoder.encode(pref.getString("accNo", null), "UTF-8") + "&" +
                            URLEncoder.encode("fname", "UTF-8") + "=" + URLEncoder.encode(fname, "UTF-8") + "&" +
                            URLEncoder.encode("lname", "UTF-8") + "=" + URLEncoder.encode(lname, "UTF-8") + "&" +
                            URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                            URLEncoder.encode("phno", "UTF-8") + "=" + URLEncoder.encode(phno, "UTF-8") + "&" +
                            URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8") + "&" +
                            URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if(pd.isShowing())
                pd.dismiss();
            if(route.equals("updateDetails")){
                if(s.equals("1"))
                    Toast.makeText(c, "Successfully Updated", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(c, "An error occurred try again later", Toast.LENGTH_LONG).show();
            }
            Log.e("getdetails", s);
        }
    }
    class CloseAsync extends AsyncTask<String, Void, String>{
        ProgressDialog pd;
        Context c;
        CloseAsync(Context c){
            this.c = c;
            pd = new ProgressDialog(c);
        }

        @Override
        protected void onPreExecute() {
            pd.setMessage("Please Wait");
            pd.setCanceledOnTouchOutside(false);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String accNo = params[0];
            String reason = params[1];
            String url_balance = "http://52.33.154.120:8080/delete.php";//"http://bom.pe.hu/delete.php";
            String result="";
            try {
                URL url = new URL(url_balance);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                OutputStream os = con.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String post = URLEncoder.encode("accNo", "UTF-8") + "=" + URLEncoder.encode(accNo, "UTF-8") + "&" +
                        URLEncoder.encode("reason", "UTF-8") + "=" + URLEncoder.encode(reason, "UTF-8");
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if(pd.isShowing())
                pd.dismiss();
            Log.e("delete", s);
            if(s.equals("1")) {
                Toast.makeText(c, "Deleted Successfully", Toast.LENGTH_LONG).show();
                Intent i = new Intent(c, MainActivity.class);
                c.startActivity(i);
            }
            else
                Toast.makeText(c, "Problem Occurred. Try again.", Toast.LENGTH_LONG).show();
        }
    }
}
