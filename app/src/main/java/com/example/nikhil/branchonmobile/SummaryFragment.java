package com.example.nikhil.branchonmobile;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryFragment extends Fragment {

    ListView lv;
    TextView empty;
    float DD,FD,Cheque,Transfer;
    SwipeRefreshLayout mSwipeRefresh;
    Fragment f;
    String accNo;
    public SummaryFragment() {
        // Required empty public constructor
        f = this;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        lv = (ListView) view.findViewById(R.id.listViewSummary);
        empty = (TextView) view.findViewById(R.id.noTransList);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.summary_refresh);
        GraphicalViewAsync ga = new GraphicalViewAsync(getContext());
        ga.execute("pie");
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GraphicalViewAsync ga = new GraphicalViewAsync(getContext(), "swipe");
                ga.execute("pie");
            }
        });
//        ArrayList<ChartItem> list = new ArrayList<ChartItem>();

        // 30 items
//        for (int i = 0; i < 40; i++) {
//
//            if(i % 4 == 0) {
//                list.add(new LineChartItem(generateDataLine(i + 1), getActivity().getApplicationContext()));
//            } else if(i % 4 == 1) {
//                list.add(new BarChartItem(generateDataBar(1), getActivity().getApplicationContext()));
//            } else if(i % 4 == 2) {
//                list.add(new PieChartItem(generateDataPie(2), getActivity().getApplicationContext()));
//            } else if(i%4 == 3){
//                list.add(new RadarChartItem(generateDataRadar(), getActivity().getApplicationContext()));
//            }
//        }

//        SummaryFragment.ChartDataAdapter cda = new SummaryFragment.ChartDataAdapter(getActivity().getApplicationContext(), list);
//        lv.setAdapter(cda);
        return view;
    }
    /** adapter that supports 3 different item types */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount() {
            return 4; // we have 3 different item-types
        }
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private LineData generateDataLine(ArrayList<Entry> e1, ArrayList<Entry> e2) {

//        ArrayList<Entry> e1 = new ArrayList<Entry>();
//
//        for (int i = 0; i < 12; i++) {
//            e1.add(new Entry(i, (int) (Math.random() * 65) + 40));
//        }

        LineDataSet d1 = new LineDataSet(e1, "Debits - Past 12 days");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setColor(Color.parseColor("#F44336"));
        d1.setCircleColor(Color.parseColor("#F44336"));
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);

//        ArrayList<Entry> e2 = new ArrayList<Entry>();

//        for (int i = 0; i < 12; i++) {
//            e2.add(new Entry(i, e1.get(i).getY() - 30));
//        }

        LineDataSet d2 = new LineDataSet(e2, "Credits - Past 12 days");
        d2.setLineWidth(2.5f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(d1);
        sets.add(d2);

        LineData cd = new LineData(sets);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private BarData generateDataBar(ArrayList<BarEntry> entry) {

        ArrayList<BarEntry> entries = entry;

//        for (int i = 0; i < 12; i++) {
//            entries.add(new BarEntry(cnt, val));
//        }

        BarDataSet d = new BarDataSet(entries, "Expenditure in the last 12 days");
        d.setColors(ColorTemplate.JOYFUL_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private PieData generateDataPie(float fd,float dd, float cheque,float transfer) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        float[] a = {fd,dd,cheque,transfer};
        String[] b = {"FD", "DD", "Cheque", "Transfer"};
        for (int i = 0; i < 4; i++) {
            if(a[i]==0)
            {}
            else
            {
                entries.add(new PieEntry(a[i], b[i]));
            }
        }

        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.JOYFUL_COLORS);
        d.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);

        PieData cd = new PieData(d);
        cd.setValueTextColor(Color.WHITE);
        return cd;
    }
    private RadarData generateDataRadar(){
        return new RadarData();
    }
    class GraphicalViewAsync extends AsyncTask<String, Void, String[]>{
        Context c;
        String[] result;
        ProgressDialog dialog;
        SharedPreferences pref;
        String type = null;
        String route;

        GraphicalViewAsync(Context c){
            result = new String[3];
            this.c = c;
            dialog = new ProgressDialog(c);
        }

        GraphicalViewAsync(Context c, String type){
            result = new String[3];
            this.c = c;
            this.type = type;
//            dialog = new ProgressDialog(c);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(type == null) {
                dialog.setMessage("Please Wait");
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        }

        @Override
        protected String[] doInBackground(String... params) {
            String url_transfer = "http://52.33.154.120:8080/graphicalview.php";//"http://bom.pe.hu/transfer.php";
            String url_bar = "http://52.33.154.120:8080/graphicalviewbar.php";
            String url_line = "http://52.33.154.120:8080/graphicalviewline.php";
            route = params[0];
            SharedPreferences pref = c.getSharedPreferences("BOM", 0);
            accNo = pref.getString("accNo", null);
            if((route.equals("pie"))) {
                try {
                    URL url = new URL(url_transfer);
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
                    result[0] = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result[0] += line;
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
                try {
                    URL url = new URL(url_bar);
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
                    result[1] = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result[1] += line;
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
            try {
                URL url = new URL(url_line);
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
                result[2] = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result[2] += line;
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
        }
            return result;
        }

        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);
            JSONArray ja = null, jaLineDebit = null, jaLineCredit = null;
            Log.e("pie data", s[0]+s[1]+s[2]);
            if (type == null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    //Toast.makeText(c, s, Toast.LENGTH_LONG).show();
                }
            } else {
                if (mSwipeRefresh.isRefreshing()) {
                    mSwipeRefresh.setRefreshing(false);
                    ParallelThread pt = new ParallelThread(f, "swipe");
                    pt.execute("balance", accNo);
                }
            }
            if (route.equals("pie")) {
                try {
                    JSONObject jo = new JSONObject(s[0]);
                    String ss = jo.getString("server_response");
                    JSONObject jo1 = new JSONObject(ss);
                    FD = Float.valueOf(jo1.getString("FD"));
                    DD = Float.valueOf(jo1.getString("DD"));
                    Cheque = Float.valueOf(jo1.getString("Cheque"));
                    Transfer = Float.valueOf(jo1.getString("Transfer"));
                    JSONObject joBar = new JSONObject(s[1]);
                    ja = joBar.getJSONArray("result");
                    JSONObject joLine = new JSONObject(s[2]);
                    jaLineDebit = joLine.getJSONArray("result");
                    jaLineCredit = joLine.getJSONArray("resultcredit");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayList<ChartItem> list = new ArrayList<ChartItem>();
                ArrayList<BarEntry> l;
                l = new ArrayList<BarEntry>();
                for (int i = 0; i < 12; i++) {
                    try {
                        l.add(new BarEntry(i+1, Integer.valueOf(ja.getString(i))));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ArrayList<Entry> e1 = new ArrayList<Entry>();
                ArrayList<Entry> e2 = new ArrayList<Entry>();
                int debitTotal = 0;
                for (int i = 0; i < 12; i++) {
                    try {
                        if(Integer.valueOf(jaLineDebit.getString(i))>0)
                            debitTotal = 1;
                        e1.add(new Entry(i+1, Integer.valueOf(jaLineDebit.getString(i))));
                        e2.add(new Entry(i+1, Integer.valueOf(jaLineCredit.getString(i))));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                list.add(new BarChartItem(generateDataBar(l), getActivity().getApplicationContext()));
                list.add(new PieChartItem(generateDataPie(FD, DD, Cheque, Transfer), getActivity().getApplicationContext()));
                list.add(new LineChartItem(generateDataLine(e1, e2), getActivity().getApplicationContext()));
                if(debitTotal == 1) {
                    Log.e("Adapter Status", "Data present");
                    SummaryFragment.ChartDataAdapter cda = new SummaryFragment.ChartDataAdapter(getActivity().getApplicationContext(), list);
                    lv.setAdapter(cda);
                }
                else{
                    mSwipeRefresh.setVisibility(View.GONE);
                    lv.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
