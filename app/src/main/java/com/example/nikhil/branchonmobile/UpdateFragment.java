package com.example.nikhil.branchonmobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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


public class UpdateFragment extends Fragment {
    private EditText fname, lname, email, phno, address, password;
    private Button update;
    private SharedPreferences pref;
    public UpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_update, container, false);
        getActivity().setTitle("Update Details");
        pref = getContext().getSharedPreferences("BOM", 0);
        final SharedPreferences.Editor editor = pref.edit();
        fname = (EditText)view.findViewById(R.id.editText5Update);
        lname = (EditText)view.findViewById(R.id.editText6Update);
        email = (EditText)view.findViewById(R.id.editText7Update);
        phno = (EditText)view.findViewById(R.id.editText8Update);
        address = (EditText)view.findViewById(R.id.editText10Update);
        password = (EditText)view.findViewById(R.id.editText4Update);
        update = (Button) view.findViewById(R.id.button2Update);
        GetDetailsAsync ua = new GetDetailsAsync(view.getContext());
        ua.execute("getDetails");
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("printLoc", "update");
                editor.putString("lname", lname.getText().toString());
                editor.putString("fname", fname.getText().toString());
                editor.putString("email", email.getText().toString());
                editor.putString("phno", phno.getText().toString());
                editor.putString("address", address.getText().toString());
                editor.putString("password", password.getText().toString());
                editor.commit();
                Intent intent = new Intent(getContext(), FingerprintActivity.class);
                getContext().startActivity(intent);
            }
        });
        return view;
    }
    class GetDetailsAsync extends AsyncTask<String, Void, String>{
        Context c;
        ProgressDialog pd;
        SharedPreferences pref;
        String route;
        GetDetailsAsync(Context c){
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
            if (route.equals("getDetails")) {
                String url_getdetails = "http://52.33.154.120:8080/getdetails.php";//"http://bom.pe.hu/getdetails.php";
                try {
                    URL url = new URL(url_getdetails);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream os = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    String post = URLEncoder.encode("accNo", "UTF-8") + "=" + URLEncoder.encode(pref.getString("accNo", null), "UTF-8");
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
            if(route.equals("getDetails")) {
                try {
                    JSONObject jo = new JSONObject(s);
                    JSONArray ja = null;
                    ja = jo.getJSONArray("server_response");
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject j = ja.getJSONObject(i);
                        fname.setText(j.getString("fname"));
                        lname.setText(j.getString("lname"));
                        email.setText(j.getString("email"));
                        phno.setText(j.getString("mobile"));
                        address.setText(j.getString("address"));
                        password.setText(j.getString("password"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
