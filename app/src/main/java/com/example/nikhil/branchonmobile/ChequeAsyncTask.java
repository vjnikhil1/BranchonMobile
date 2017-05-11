package com.example.nikhil.branchonmobile;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.List;

/**
 * Created by Nikhil on 16-03-2017.
 */

public class ChequeAsyncTask extends AsyncTask<String,Void,String> {
    Activity c;
    ProgressDialog dialog;
    String route;
    String result;
    RecyclerView recyclerView;
    ChequesAdapter adapter;
    Fragment a;
    ChequeAsyncTask(Activity c){
        this.c = c;
        dialog = new ProgressDialog(c);
    }
    ChequeAsyncTask(Fragment a){
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
                        URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode("50", "UTF-8") + "&" +
                        URLEncoder.encode("cheque_img", "UTF-8") + "=" + URLEncoder.encode(params[5], "UTF-8");
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
        else if(route.equals("getChequeDetails")){
            String url_getdetails = "http://52.33.154.120:8080/chequedetails.php";
            String rAccNo = params[1];
            try {
                URL url = new URL(url_getdetails);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                OutputStream os = con.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String post = URLEncoder.encode("rAccNo", "UTF-8") + "=" + URLEncoder.encode(rAccNo, "UTF-8");
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
        else if(route.equals("getChequeDetails")){
            List<Cheques> totArray = new ArrayList<>();
            try {
                JSONObject jo = new JSONObject(s);
                JSONArray ja = jo.getJSONArray("server_response");
                for(int i=0;i<ja.length();i++){
                    JSONObject j = ja.getJSONObject(i);
                    Cheques c = new Cheques(j.getString("sAccNo"), j.getString("amount"), j.getString("date"), j.getString("status")
                                            ,j.getString("process"));
                    totArray.add(c);

                }
                recyclerView = (RecyclerView) a.getActivity().findViewById(R.id.recyclerView);
                RecyclerView.LayoutManager lm = new LinearLayoutManager(c);
                adapter = new ChequesAdapter(totArray);
                recyclerView.setLayoutManager(lm);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
