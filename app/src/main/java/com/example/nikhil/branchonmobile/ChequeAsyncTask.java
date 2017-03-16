package com.example.nikhil.branchonmobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Nikhil on 16-03-2017.
 */

public class ChequeAsyncTask extends AsyncTask<String,Void,String> {
    Activity c;
    ProgressDialog dialog;
    String route;
    String result;
    ChequeAsyncTask(Activity c){
        this.c = c;
        dialog = new ProgressDialog(c);
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
        String url_register = /*"http://bom.pe.hu/register.php";*/"http://52.33.154.120:8080/chequeinsert.php";
        route = params[0];
        if(route.equals("insert")){
            String sAccNo = params[1];
            String rAccNo = params[2];
            String date = params[3];
            String amount = params[4];
            try {
                URL url = new URL(url_register);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                OutputStream os = con.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String post = URLEncoder.encode("sAccNo", "UTF-8") + "=" + URLEncoder.encode(sAccNo, "UTF-8") + "&" +
                        URLEncoder.encode("rAccNo", "UTF-8") + "=" + URLEncoder.encode(rAccNo, "UTF-8") + "&" +
                        URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") + "&" +
                        URLEncoder.encode("amount", "UTF-8") + "=" + URLEncoder.encode(amount, "UTF-8") + "&" +
                        URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode("50", "UTF-8");
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
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.e("cheque insert", s);
        if(dialog.isShowing()){
            dialog.dismiss();
        }
        if(route.equals("insert")){
            if(s.equals("1")){
                Toast.makeText(c, "Successfully Processed", Toast.LENGTH_LONG).show();
                Log.e("cheque insert", s);
            }
            else{
                Toast.makeText(c, "An error occurred" + s, Toast.LENGTH_LONG).show();
            }
        }
    }
}
