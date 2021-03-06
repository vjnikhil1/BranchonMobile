package com.example.nikhil.branchonmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.fingerprint.FingerprintManager;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
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
 * Created by Nikhil on 04-02-2017.
 */

class ParallelThread extends AsyncTask<String, Void, String> {
    Context c;
    String result = "", type = null;
    Fragment a;
    ProgressDialog dialog;
    SharedPreferences pref;
    String acc_no;
    ParallelThread(Context c){
        this.c = c;
        dialog = new ProgressDialog(c);
    }
    ParallelThread(Fragment a){
        this.a = a;
        dialog = new ProgressDialog(a.getContext());
    }
    ParallelThread(Fragment a, String type){
        this.a = a;
        this.type = type;
//        dialog = new ProgressDialog(a.getContext());
    }
    String route;
    @Override
    protected String doInBackground(String... params) {
        String url_register = /*"http://bom.pe.hu/register.php";*/"http://52.33.154.120:8080/register.php";
        route = params[0];
        if (route == "register") {
            String first_name = params[1];
            String last_name = params[2];
            String email = params[3];
            String mobile = params[4];
            String acc_type = params[5];
            String pan = params[6];
            String address = params[7];
            String password = params[8];
            String panNo = params[9];
            String aadhaarNo = params[10];
            String aadhaarImg = params[11];
            String signatureImg = params[12];
            String dob = params[13];
            String signatureImg1 = params[14];
            String signatureImg2 = params[15];

            try {
                URL url = new URL(url_register);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                OutputStream os = con.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String post = URLEncoder.encode("first_name", "UTF-8") + "=" + URLEncoder.encode(first_name, "UTF-8") + "&" +
                        URLEncoder.encode("last_name", "UTF-8") + "=" + URLEncoder.encode(last_name, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("mobile", "UTF-8") + "=" + URLEncoder.encode(mobile, "UTF-8") + "&" +
                        URLEncoder.encode("acc_type", "UTF-8") + "=" + URLEncoder.encode(acc_type, "UTF-8") + "&" +
                        URLEncoder.encode("pan", "UTF-8") + "=" + URLEncoder.encode(pan, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" +
                        URLEncoder.encode("panNo", "UTF-8") + "=" + URLEncoder.encode(panNo, "UTF-8") + "&" +
                        URLEncoder.encode("aadhaarNo", "UTF-8") + "=" + URLEncoder.encode(aadhaarNo, "UTF-8") + "&" +
                        URLEncoder.encode("aadhaarImg", "UTF-8") + "=" + URLEncoder.encode(aadhaarImg, "UTF-8") + "&" +
                        URLEncoder.encode("signatureImg", "UTF-8") + "=" + URLEncoder.encode(signatureImg, "UTF-8") + "&" +
                        URLEncoder.encode("signatureImg1", "UTF-8") + "=" + URLEncoder.encode(signatureImg1, "UTF-8") + "&" +
                        URLEncoder.encode("signatureImg2", "UTF-8") + "=" + URLEncoder.encode(signatureImg2, "UTF-8") + "&" +
                        URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8") + "&" +
                        URLEncoder.encode("dob", "UTF-8") + "=" + URLEncoder.encode(dob, "UTF-8");
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
        }
        else if(route=="login"){
            String url_loginauth = "http://52.33.154.120:8080/loginauth.php";//"http://bom.pe.hu/loginauth.php";
            acc_no = params[1];
            String password = params[2];
            pref = c.getSharedPreferences("BOM", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("accNo", acc_no);
            editor.commit();
            try {
                URL url = new URL(url_loginauth);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                OutputStream os = con.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String post = URLEncoder.encode("acc_no", "UTF-8") + "=" + URLEncoder.encode(acc_no, "UTF-8") + "&" +
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
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(route.equals("balance")){
            String url_balance = "http://52.33.154.120:8080/balance.php";//"http://bom.pe.hu/balance.php";
            String accNo = params[1];
            try {
                URL url = new URL(url_balance);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                OutputStream os = con.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String post = URLEncoder.encode("accNo", "UTF-8") + "=" + URLEncoder.encode(accNo, "UTF-8");
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
        }
        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //if(route.equals("register")){
        if(type == null) {
            dialog.setMessage("Please Wait");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        //}
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(route=="register") {
            if(dialog.isShowing()){
                dialog.dismiss();
            }
            Toast.makeText(c, s, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(c, MainActivity.class);
            c.startActivity(intent);
            //Intent ij = new Intent(c, FingerprintActivity.class);
            //c.startActivity(ij);
        }
        else if(route=="login"){
            if(dialog.isShowing())
                dialog.dismiss();
            Toast.makeText(c,s,Toast.LENGTH_LONG).show();
            if(!s.equals("Wrong Credentials")) {
                SharedPreferences p = c.getSharedPreferences("BOM",0);
                SharedPreferences.Editor editor = p.edit();
                editor.putString("accName",s);
                editor.putString("accNo", acc_no);
                editor.commit();
                DBHelper db = new DBHelper(c);
                Cursor res = db.getData();
                while(res.moveToNext()){
                    Log.e("Cloud Push", res.getString(0)+res.getString(1));
                    FingerprintHandler fh = new FingerprintHandler(c);
                    FingerprintHandler.TransferAsync ta = fh.new TransferAsync(c);
                    ta.execute("FD",    ((Integer.parseInt(res.getString(1)))+
                            (Integer.parseInt(res.getString(0))*(Integer.parseInt(res.getString(1))/100)))+"");
                }
                db.dropTable();
                Intent intent = new Intent(c, FingerprintActivity.class);
                SharedPreferences.Editor editor1 = pref.edit();
                editor1.putString("printLoc", "login");
                editor1.commit();
                c.startActivity(intent);
            }
        }
        else if(route.equals("balance")){
            DBHelper db = new DBHelper(a.getContext());
            Cursor res = db.getData();
            while(res.moveToNext()){
                Log.e("Cloud Push", res.getString(0)+res.getString(1));
                FingerprintHandler fh = new FingerprintHandler(a.getContext());
                FingerprintHandler.TransferAsync ta = fh.new TransferAsync(a.getContext(), "FD");
                ta.execute("FD",    ((Integer.parseInt(res.getString(1)))+
                        (Integer.parseInt(res.getString(0))*(Integer.parseInt(res.getString(1))/100)))+"");
            }
            db.dropTable();
            Log.e("balance", s);
            String bal = "";
            try {
                JSONObject jo = new JSONObject(s);
                JSONArray ja = jo.getJSONArray("server_response");
                for(int i=0;i<ja.length();i++){
                    JSONObject j = ja.getJSONObject(i);
                    bal = j.getString("balance");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }           TextView balance = (TextView) a.getActivity().findViewById(R.id.textView5);
            balance.setSingleLine(false);
            String temp = "₹ "+bal+"/-";
            SpannableString sp = new SpannableString("Current Wallet Balance");
            SpannableString sp2 = new SpannableString("₹ "+bal+"/-");
            int textSize1 = a.getContext().getResources().getDimensionPixelSize(R.dimen.text_size_1);
            int textSize2 = a.getContext().getResources().getDimensionPixelSize(R.dimen.text_size_2);
            sp.setSpan(new AbsoluteSizeSpan(textSize1), 0, "Current Wallet Balance".length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            sp2.setSpan(new AbsoluteSizeSpan(textSize2), 0 , temp.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            CharSequence finalText =TextUtils.concat(sp,"\n",sp2);
            balance.setText(finalText);
            if(type == null) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            if(Integer.parseInt(bal)<5000){
                AlertDialog.Builder ab = new AlertDialog.Builder(a.getContext());
                ab.setTitle("Insufficient funds");
                ab.setMessage("Please add ₹" + (5000-Integer.parseInt(bal)) + "/- into your account to continue using your account.");
                AlertDialog ad = ab.create();
                ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((HomeActivity)a.getActivity()).openDialog();
                    }
                });
                ab.setCancelable(false);
                ab.show();
            }
        }
        //Log.e("test",s);
    }
}
