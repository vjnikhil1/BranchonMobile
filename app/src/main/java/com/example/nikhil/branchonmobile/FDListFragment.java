package com.example.nikhil.branchonmobile;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * A simple {@link Fragment} subclass.
 */
public class FDListFragment extends Fragment {
    private RecyclerView recyclerView;
    private FDAdapter fdAdapter;
    private SharedPreferences pref;


    public FDListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fdlist, container, false);
        pref = getActivity().getSharedPreferences("BOM", 0);
        String accNo = pref.getString("accNo", null);
        FDAsyncTask fa = new FDAsyncTask(this);
        fa.execute(accNo);
        return view;
    }

    public class FDAsyncTask extends AsyncTask<String,Void,String> {
        Activity c;
        ProgressDialog dialog;
        String route;
        String result;
        RecyclerView recyclerView;
        FDAdapter adapter;
        Fragment a;
        FDAsyncTask(Activity c){
            this.c = c;
            dialog = new ProgressDialog(c);
        }
        FDAsyncTask(Fragment a){
            this.a = a;
            dialog = new ProgressDialog(a.getActivity());
        }
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please Wait");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
                String url_getdetails = "http://52.33.154.120:8080/fdlist.php";
                String accNo = params[0];
                try {
                    URL url = new URL(url_getdetails);
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
            Log.e("FD List", s);
            if(dialog.isShowing()){
                dialog.dismiss();
            }
                List<FDItem> totArray = new ArrayList<>();
                try {
                    JSONObject jo = new JSONObject(s);
                    JSONArray ja = jo.getJSONArray("server_response");
                    for(int i=0;i<ja.length();i++){
                        JSONObject j = ja.getJSONObject(i);
                        FDItem c = new FDItem(j.getString("id"), j.getString("amount"), j.getString("date"), j.getString("time"), j.getString("period"));
                        totArray.add(c);

                    }
                    recyclerView = (RecyclerView) a.getActivity().findViewById(R.id.recyclerView);
                    RecyclerView.LayoutManager lm = new LinearLayoutManager(c);
                    adapter = new FDAdapter(totArray);
                    recyclerView.setLayoutManager(lm);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }
}
