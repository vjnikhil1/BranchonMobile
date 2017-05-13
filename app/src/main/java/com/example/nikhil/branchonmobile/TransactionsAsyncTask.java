package com.example.nikhil.branchonmobile;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil on 23-03-2017.
 */

public class TransactionsAsyncTask extends AsyncTask<String, Void, String> {
    ProgressDialog dialog;
    String accNo;
    String result;
    RecyclerView recyclerView;
    TransactionAdapter adapter;
    Fragment a;
    SharedPreferences pref;
    String type = null;

    public TransactionsAsyncTask(Fragment a) {
        this.dialog = new ProgressDialog(a.getActivity());
        this.a = a;
        pref = a.getContext().getSharedPreferences("BOM", 0);
    }

    public TransactionsAsyncTask(Fragment a, String type) {
        //this.dialog = new ProgressDialog(a.getActivity());
        this.type = type;
        this.a = a;
        pref = a.getContext().getSharedPreferences("BOM", 0);
    }

    @Override
    protected void onPreExecute() {
        if(type==null) {
            dialog.setMessage("Please Wait");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String url_register = /*"http://bom.pe.hu/register.php";*/"http://52.33.154.120:8080/gettransactions.php";
        accNo = params[0];
            try {
                URL url = new URL(url_register);
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
            }
            catch (Exception e){
                e.printStackTrace();
            }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
            Log.e("transactionFetch", s);
        if(type==null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        else{
            SwipeRefreshLayout mSwipe = (SwipeRefreshLayout) a.getActivity().findViewById(R.id.transaction_refresh);
            if(mSwipe.isRefreshing()){
                mSwipe.setRefreshing(false);
                ParallelThread pt = new ParallelThread(a, "swipe");
                pt.execute("balance", pref.getString("accNo", null));
            }
        }
            List<Transaction> totArray = new ArrayList<>();
            try {
                JSONObject jo = new JSONObject(s);
                JSONArray ja = jo.getJSONArray("server_response");
                for(int i=0;i<ja.length();i++){
                    JSONObject j = ja.getJSONObject(i);
                    if(pref.getString("accNo", null).equals(j.getString("sAccNo"))){
                        totArray.add(new Transaction(R.drawable.debit, "Cash Sent", "To: " + j.getString("rAccNo"),
                                j.getString("date")+", "+j.getString("time"), "₹ "+j.getString("amount")+"/-"));
                    }
                    else{
                        totArray.add(new Transaction(R.drawable.credit, "Cash Received", "From: " + j.getString("sAccNo"),
                                j.getString("date")+", "+j.getString("time"), "₹ " + j.getString("amount")+"/-"));
                    }
                }
                if(totArray.isEmpty()){
                    Log.e("No Transactions",s);
                    TextView noTrans = (TextView) a.getActivity().findViewById(R.id.noTrans);
                    noTrans.setVisibility(View.VISIBLE);
                }
                recyclerView = (RecyclerView) a.getActivity().findViewById(R.id.recyclerView2);
                RecyclerView.LayoutManager lm = new LinearLayoutManager(a.getContext());
                adapter = new TransactionAdapter(totArray);
                recyclerView.setLayoutManager(lm);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }
}
