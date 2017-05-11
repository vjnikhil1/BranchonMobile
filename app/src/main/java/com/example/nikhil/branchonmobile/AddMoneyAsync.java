package com.example.nikhil.branchonmobile;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
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
 * Created by Nikhil on 02-04-2017.
 */

public class AddMoneyAsync extends AsyncTask<String, Void, String> {
    ProgressDialog dialog;
    String amt;
    String accNo;
    String result;
    Fragment a;
    SharedPreferences pref;

    AddMoneyAsync(Fragment a){
        this.dialog = new ProgressDialog(a.getActivity());
        this.a = a;
        pref = a.getContext().getSharedPreferences("BOM", 0);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Please Wait");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected String doInBackground(String[] params) {
        String url_register = /*"http://bom.pe.hu/register.php";*/"http://52.33.154.120:8080/addmoney.php";
        amt=params[0];
        accNo = pref.getString("accNo", null);
        try {
            URL url = new URL(url_register);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            OutputStream os = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String post = URLEncoder.encode("accNo", "UTF-8") + "=" + URLEncoder.encode(accNo, "UTF-8") + "&" +
                    URLEncoder.encode("amount", "UTF-8") + "=" + URLEncoder.encode(amt, "UTF-8");
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
    protected void onPostExecute(String o) {
        if(dialog.isShowing())
            dialog.dismiss();
        if(o.equals("1"))
            Toast.makeText(a.getContext(), "Money Added Successfully, Please Check your account", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(a.getContext(), "A problem has occurred. Please try again.", Toast.LENGTH_LONG).show();
    }
}
