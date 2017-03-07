package com.example.nikhil.branchonmobile;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
 * A simple {@link Fragment} subclass.
 */
public class CloseFragment extends Fragment {
    SharedPreferences pref;
    private EditText no;
    private Button close;

    public CloseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_close, container, false);
        pref = this.getActivity().getSharedPreferences("BOM",0);
        final String accNo = pref.getString("accNo", null);
        no = (EditText) view.findViewById(R.id.closeAccNo);
        close = (Button) view.findViewById(R.id.button6);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accNoIn = no.getText().toString();
                if(!accNoIn.equals(accNo))
                    Toast.makeText(view.getContext(),"Wrong Number Entered", Toast.LENGTH_SHORT).show();
                else{
                    CloseAsync ca = new CloseAsync(view.getContext());
                    ca.execute(accNoIn);
                }
            }
        });
        return view;
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
