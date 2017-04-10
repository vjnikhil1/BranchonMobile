package com.example.nikhil.branchonmobile;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class DDFragment extends Fragment {
    private File file;
    private SharedPreferences pref;
    private EditText payable, amount, amountWord;
    private Button generate;

    public DDFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dd, container, false);
        getActivity().setTitle("Demand Draft");
        pref = getActivity().getSharedPreferences("BOM",0);
        final SharedPreferences.Editor editor = pref.edit();
        payable = (EditText) view.findViewById(R.id.editText14);
        amount = (EditText) view.findViewById(R.id.editText13);
        generate = (Button) view.findViewById(R.id.buttonDD);
        amountWord = (EditText) view.findViewById(R.id.editText15);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("printLoc","DD");
                editor.putString("DDPay", payable.getText().toString());
                editor.putString("DDAmount", amount.getText().toString());
                editor.putString("DDAmountWord", amountWord.getText().toString());
                editor.commit();
                Intent intent = new Intent(getContext(), FingerprintActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}