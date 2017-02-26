package com.example.nikhil.branchonmobile;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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


public class TransferFundsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText rec, amount;
    private Button send;


    public TransferFundsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TransferFundsFragment newInstance(String param1, String param2) {
        TransferFundsFragment fragment = new TransferFundsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transfer_funds, container, false);
        rec = (EditText) view.findViewById(R.id.editTextReceiver);
        amount = (EditText) view.findViewById(R.id.amount);
        send = (Button) view.findViewById(R.id.transfer);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferAsync ta = new TransferAsync(getActivity());
                ta.execute(rec.getText().toString(), amount.getText().toString());
            }
        });
        return view;
    }
    class TransferAsync extends AsyncTask<String,Void,String>{
        Activity a;
        ProgressDialog dialog;
        String result;
        TransferAsync(Activity a){
            this.a = a;
            dialog = new ProgressDialog(a);
        }
        @Override
        protected String doInBackground(String... params) {
            String url_transfer = "http://bom.pe.hu/transfer.php";
            String receiver = params[0];
            String amount = params[1];
            SharedPreferences pref = getActivity().getSharedPreferences("BOM", 0);
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
            Toast.makeText(a.getApplicationContext(),s,Toast.LENGTH_LONG).show();
            Log.e("transfer", s);
        }
    }
}
