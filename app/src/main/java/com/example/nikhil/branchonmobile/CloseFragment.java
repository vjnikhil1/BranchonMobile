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
    private EditText no, reasonText;
    private Button close;

    public CloseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_close, container, false);
//        getActivity().setTitle("Close Account");
        pref = this.getActivity().getSharedPreferences("BOM",0);
        final String accNo = pref.getString("accNo", null);
        final SharedPreferences.Editor editor = pref.edit();
        no = (EditText) view.findViewById(R.id.closeAccNo);
        close = (Button) view.findViewById(R.id.button6);
        reasonText = (EditText) view.findViewById(R.id.editText2);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accNoIn = no.getText().toString();
                String reason = reasonText.getText().toString();
                if(!accNoIn.equals(accNo))
                    Toast.makeText(view.getContext(),"Wrong Number Entered", Toast.LENGTH_SHORT).show();
                else if(reason.isEmpty()){
                    Toast.makeText(view.getContext(),"Enter a valid reason", Toast.LENGTH_SHORT).show();
                }
                else{
                    editor.putString("printLoc", "close");
                    editor.putString("accNoIn", accNoIn);
                    editor.putString("reason", reason);
                    editor.commit();
                    Intent intent = new Intent(getContext(), FingerprintActivity.class);
                    getContext().startActivity(intent);
                }
            }
        });
        return view;
    }
}
