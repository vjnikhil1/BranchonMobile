package com.example.nikhil.branchonmobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

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
 * Created by Nikhil on 26-02-2017.
 */

public class FirebaseAsyncTask extends AsyncTask<String,Void,String> {
    private String result;
    private Context context;

    public FirebaseAsyncTask(Context c)
    {
        context = c;
    }
    @Override
    protected String doInBackground(String... params) {
        String token = params[0];
        String accName = params[1];
        Log.e("token",token+" "+accName);
        String url_token = "http://bom.pe.hu/token.php";
        try {
            URL url = new URL(url_token);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            OutputStream os = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String post = URLEncoder.encode("Token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8") + "&" +
                    URLEncoder.encode("firstname", "UTF-8") + "=" + URLEncoder.encode(accName, "UTF-8");
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
            Log.e("token", result);
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
    protected void onPostExecute(String s) {
        Log.e("token", s);
    }
}
