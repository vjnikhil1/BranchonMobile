package com.example.nikhil.branchonmobile;


import android.app.Activity;
import android.app.Dialog;
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


public class TransferFundsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText rec, amount, password;
    private Button send;
    private SharedPreferences pref;


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
//        getActivity().setTitle("Transfer Funds");
        rec = (EditText) view.findViewById(R.id.editTextReceiver);
        amount = (EditText) view.findViewById(R.id.amount);
        send = (Button) view.findViewById(R.id.transfer);
        password = (EditText) view.findViewById(R.id.password);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FingerprintActivity.class);
                if(amount.getText().toString().isEmpty()||rec.getText().toString().isEmpty()
                        ||password.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Enter all the fields", Toast.LENGTH_LONG).show();
                }
                else if(Integer.valueOf(amount.getText().toString())<=0) {
                    Toast.makeText(getContext(), "Invalid amount", Toast.LENGTH_LONG).show();
                }
                else if(Integer.valueOf(amount.getText().toString())>1000000){
                    Toast.makeText(getContext(), "Only a maximum transaction of 1000000 can be done at a time"
                            ,Toast.LENGTH_LONG).show();
                }
                else {
                    pref = getActivity().getSharedPreferences("BOM", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("printLoc", "transfer");
                    editor.putString("rec", rec.getText().toString());
                    editor.putString("amount", Integer.valueOf(amount.getText().toString()).toString());
                    editor.putString("password", password.getText().toString());
                    editor.commit();
                    amount.setText("");
                    rec.setText("");
                    password.setText("");
                    getContext().startActivity(intent);
                }
            }
        });
        return view;
    }
}
