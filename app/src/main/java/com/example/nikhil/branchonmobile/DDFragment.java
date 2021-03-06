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
import android.widget.Toast;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.commons.lang3.text.WordUtils;

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
    private EditText payable, amount, amountWord, password;
    private Button generate;

    public DDFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dd, container, false);
//        getActivity().setTitle("Demand Draft");
        pref = getActivity().getSharedPreferences("BOM",0);
        final SharedPreferences.Editor editor = pref.edit();
        payable = (EditText) view.findViewById(R.id.editText14);
        amount = (EditText) view.findViewById(R.id.editText13);
        generate = (Button) view.findViewById(R.id.buttonDD);
        amountWord = (EditText) view.findViewById(R.id.editText15);
        password = (EditText) view.findViewById(R.id.password);
        amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if(!amount.getText().toString().equals("")) {
                        if(Integer.valueOf(amount.getText().toString())>0) {
                            if(Integer.valueOf(amount.getText().toString())>1000000){
                                Toast.makeText(getContext(), "Only a maximum transaction of 1000000 can be done at a time"
                                ,Toast.LENGTH_LONG).show();
                            }
                            else {
                                NumToWords numToWords = new NumToWords();
                                amountWord.setText(WordUtils.capitalizeFully(numToWords.convert(
                                        Integer.valueOf(amount.getText().toString()))) + " Rupees");
                            }
                        }
                    }
                }
            }
        });
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(payable.getText().toString().isEmpty()||amount.getText().toString().isEmpty()
                        ||amountWord.getText().toString().isEmpty()||password.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Enter all the fields", Toast.LENGTH_LONG).show();
                }
                else if(Integer.valueOf(amount.getText().toString())<=0) {
                    Toast.makeText(getContext(), "Enter a valid amount", Toast.LENGTH_LONG).show();
                }
                else {
                    editor.putString("printLoc", "DD");
                    editor.putString("DDPay", payable.getText().toString());
                    editor.putString("DDAmount", Integer.valueOf(amount.getText().toString()).toString());
                    editor.putString("DDAmountWord", amountWord.getText().toString());
                    editor.putString("password", password.getText().toString());
                    editor.commit();
                    amount.setText("");
                    payable.setText("");
                    amountWord.setText("");
                    password.setText("");
                    Intent intent = new Intent(getContext(), FingerprintActivity.class);
                    startActivity(intent);
                }
            }
        });
        return view;
    }
}