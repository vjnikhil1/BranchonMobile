package com.example.nikhil.branchonmobile;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class FDFragment extends Fragment {
    private EditText time, amount, password;
    private Button sub;
    private FloatingActionButton fab;


    public FDFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fd, container, false);
//        getActivity().setTitle("Fixed Deposit");
        time = (EditText) view.findViewById(R.id.editText16);
        amount = (EditText) view.findViewById(R.id.famount);
        password = (EditText) view.findViewById(R.id.password);
        sub = (Button) view.findViewById(R.id.button7);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.floatingActionButton1);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time.getText().toString().isEmpty()||amount.getText().toString().isEmpty()
                        ||password.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Enter all the fields", Toast.LENGTH_LONG).show();
                }
                else if(Integer.valueOf(amount.getText().toString())<=0) {
                    Toast.makeText(getContext(), "Enter a valid amount", Toast.LENGTH_LONG).show();
                }
                else {
                    FDAsync fa = new FDAsync(getContext());
                    fa.execute(amount.getText().toString(), password.getText().toString());
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.hide();
                getActivity().getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_home, new FDListFragment())
                        .addToBackStack("8")
                        .commit();
            }
        });
        return view;
    }
    class FDAsync extends AsyncTask<String, Void, String>{
        Context c;
        String result = "";
        ProgressDialog dialog;
        SharedPreferences pref;
        FDAsync(Context c){
            this.c = c;
            dialog = new ProgressDialog(c);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please Wait");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url_transfer = "http://52.33.154.120:8080/fdprocess.php";//"http://bom.pe.hu/transfer.php";
            String amount = params[0];
            String password = params[1];
            SharedPreferences pref = c.getSharedPreferences("BOM", 0);
            String accNo = pref.getString("accNo", null);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss");
            Date obj = new Date();
            try {
                URL url = new URL(url_transfer);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                OutputStream os = con.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String post = URLEncoder.encode("accNo", "UTF-8") + "=" + URLEncoder.encode(accNo, "UTF-8") + "&" +
                        URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(df.format(obj), "UTF-8") + "&" +
                        URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(df1.format(obj), "UTF-8") + "&" +
                        URLEncoder.encode("amount", "UTF-8") + "=" + URLEncoder.encode(amount, "UTF-8") + "&" +
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
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog.isShowing()) {
                dialog.dismiss();
                Toast.makeText(c, s, Toast.LENGTH_LONG).show();
            }
            if(s.equals("Please check your transaction password")){
                Toast.makeText(c,s,Toast.LENGTH_LONG).show();
                Intent i = new Intent(c, HomeActivity.class);
                c.startActivity(i);
            }
            else {
                Toast.makeText(getContext(), "FD Successfully Created", Toast.LENGTH_LONG).show();
                AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
                Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
                notificationIntent.putExtra("amount", amount.getText().toString());
                notificationIntent.putExtra("period", time.getText().toString());
                notificationIntent.addCategory("android.intent.category.DEFAULT");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), new Random().nextInt(), notificationIntent, PendingIntent.FLAG_ONE_SHOT);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, Integer.parseInt(time.getText().toString()));
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }
}
