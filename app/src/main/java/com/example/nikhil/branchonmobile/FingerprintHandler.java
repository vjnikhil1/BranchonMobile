package com.example.nikhil.branchonmobile;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.AsyncTask;
import android.os.CancellationSignal;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import java.util.Date;


/**
 * Created by Nikhil on 06-02-2017.
 */

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private Context context;
    private SharedPreferences pref;
    SharedPreferences.Editor editor;


    // Constructor
    public FingerprintHandler(Context mContext) {
        context = mContext;
            pref = context.getSharedPreferences("BOM", 0);
            editor = pref.edit();
    }


    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint Authentication error\n" + errString, false);
    }


    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString, false);
    }


    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.", false);
    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Fingerprint Authentication succeeded.", true);
        success();
    }

    public void success(){
        if(pref.getString("printLoc", null).equals("login")) {
            editor.putString("printLoc", null);
            Intent intent = new Intent(context, HomeActivity.class);
            context.startActivity(intent);
        }
        else if(pref.getString("printLoc", null).equals("transfer")){
            editor.putString("printLoc", null);
            TransferAsync ta = new TransferAsync(context);
            ta.execute(pref.getString("rec", null), pref.getString("amount", null));
            Intent intent = new Intent(context, HomeActivity.class);
            context.startActivity(intent);
        }
        else if(pref.getString("printLoc", null).equals("update")){
            editor.putString("printLoc", null);
            UpdateAsync ua = new UpdateAsync(context);
            ua.execute("updateDetails", pref.getString("fname", null),
                    pref.getString("lname", null),
                    pref.getString("dob", null),
                    pref.getString("email", null),
                    pref.getString("phno", null),
                    pref.getString("address", null),
                    pref.getString("password", null));
            Intent intent = new Intent(context, HomeActivity.class);
            context.startActivity(intent);
        }
        else if(pref.getString("printLoc", null).equals("close")){
            editor.putString("printLoc", null);
            CloseAsync ca = new CloseAsync(context);
            ca.execute(pref.getString("accNoIn", null), pref.getString("reason", null));
        }
        else if(pref.getString("printLoc", null).equals("DD")){
            editor.putString("printLoc", null);
            DDAsync da = new DDAsync(context);
            da.execute(pref.getString("DDPay", null), pref.getString("DDAmount", null), pref.getString("DDAmountWord", null));
        }
    }

    public void update(String e, Boolean success){
        TextView textView = (TextView) ((Activity)context).findViewById(R.id.errorText);
        textView.setText(e);
        if(success){
            textView.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        }
    }
    class TransferAsync extends AsyncTask<String,Void,String> {
        Context c;
        ProgressDialog dialog;
        String result;
        TransferAsync(Context c){
            this.c = c;
            dialog = new ProgressDialog(c);
        }
        @Override
        protected String doInBackground(String... params) {
            String url_transfer = "http://52.33.154.120:8080/transfer.php";//"http://bom.pe.hu/transfer.php";
            String receiver = params[0];
            String amount = params[1];
            SharedPreferences pref = c.getSharedPreferences("BOM", 0);
            String accNo = pref.getString("accNo", null);
            try {
                URL url = new URL(url_transfer);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                OutputStream os = con.getOutputStream();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat df1 = new SimpleDateFormat("HH:mm:ss");
                Date obj = new Date();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String post = URLEncoder.encode("accNo", "UTF-8") + "=" + URLEncoder.encode(accNo, "UTF-8") + "&" +
                        URLEncoder.encode("amount", "UTF-8") + "=" + URLEncoder.encode(amount, "UTF-8") + "&" +
                        URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(df.format(obj), "UTF-8") + "&" +
                        URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(df1.format(obj), "UTF-8") + "&" +
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
            try {
                if(dialog.isShowing()&&dialog!=null)
                    dialog.dismiss();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                dialog = null;
                Toast.makeText(context,s,Toast.LENGTH_LONG).show();
                Log.e("transfer", s);
            }
        }
    }
    class UpdateAsync extends AsyncTask<String, Void, String>{
        Context c;
        ProgressDialog pd;
        SharedPreferences pref;
        String route;
        UpdateAsync(Context c){
            this.c = c;
            pd = new ProgressDialog(c);
            pref = c.getSharedPreferences("BOM", 0);
        }

        @Override
        protected void onPreExecute() {
            pd.setMessage("Please Wait");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            route = params[0];
            if(route.equals("updateDetails")){
                String fname = params[1];
                String lname = params[2];
                String dob = params[3];
                String email = params[4];
                String phno = params[5];
                String address = params[6];
                String password = params[7];
                String url_updatedetails = "http://52.33.154.120:8080/updatedetails.php";//"http://bom.pe.hu/updatedetails.php";
                try {
                    URL url = new URL(url_updatedetails);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream os = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    String post = URLEncoder.encode("accNo", "UTF-8") + "=" + URLEncoder.encode(pref.getString("accNo", null), "UTF-8") + "&" +
                            URLEncoder.encode("fname", "UTF-8") + "=" + URLEncoder.encode(fname, "UTF-8") + "&" +
                            URLEncoder.encode("lname", "UTF-8") + "=" + URLEncoder.encode(lname, "UTF-8") + "&" +
                            URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                            URLEncoder.encode("phno", "UTF-8") + "=" + URLEncoder.encode(phno, "UTF-8") + "&" +
                            URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8") + "&" +
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if(pd.isShowing())
                pd.dismiss();
            if(route.equals("updateDetails")){
                if(s.equals("1"))
                    Toast.makeText(c, "Successfully Updated", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(c, "An error occurred try again later", Toast.LENGTH_LONG).show();
            }
            Log.e("getdetails", s);
        }
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
            String reason = params[1];
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
                String post = URLEncoder.encode("accNo", "UTF-8") + "=" + URLEncoder.encode(accNo, "UTF-8") + "&" +
                        URLEncoder.encode("reason", "UTF-8") + "=" + URLEncoder.encode(reason, "UTF-8");
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
    class DDAsync extends AsyncTask<String,Void,String>{
        ProgressDialog pd;
        Context c;
        File file;
        DDAsync(Context c){
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
            String payable = params[0];
            String amount = params[1];
            String amountWord = params[2];
            return generatePdf(payable, amount, amountWord);
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("1")) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(FileProvider.getUriForFile(c, "com.example.nikhil.branchonmobile", file), "application/pdf");
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                c.startActivity(i);
            }
            else
                Toast.makeText(c, "Failed!", Toast.LENGTH_LONG).show();
        }
        private String generatePdf(String payable, String amount, String amountWord) {
            try {
                String payableFinal="";
                String amountFinal="";
                String amountWordFinal="";
                amountWord+=" only";
                File fileDir = c.getExternalFilesDir(Environment.DIRECTORY_PICTURES+"/DD's");
                if (!fileDir.exists())
                    fileDir.mkdirs();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                for(int i=0;i<14-amount.length();i++){
                    amountFinal+="*";
                }
                amountFinal+=amount;
                if(payable.length()%2==0){
                    for(int i=0;i<20-(payable.length()/2);i++){
                        payableFinal+="*";
                    }
                    payableFinal+=payable;
                    for(int i=0;i<20-(payable.length()/2);i++){
                        payableFinal+="*";
                    }
                }
                else{
                    for(int i=0;i<20-(payable.length()/2);i++){
                        payableFinal+="*";
                    }
                    payableFinal+=payable;
                    for(int i=0;i<21-(payable.length()/2);i++){
                        payableFinal+="*";
                    }
                }
                if(amountWord.length()%2==0){
                    for(int i=0;i<40-(amountWord.length()/2);i++){
                        amountWordFinal+="*";
                    }
                    amountWordFinal+=amountWord;
                    for(int i=0;i<41-(amountWord.length()/2);i++){
                        amountWordFinal+="*";
                    }
                }
                else{
                    for(int i=0;i<40-(amountWord.length()/2);i++){
                        amountWordFinal+="*";
                    }
                    amountWordFinal+=amountWord;
                    for(int i=0;i<40-(amountWord.length()/2);i++){
                        amountWordFinal+="*";
                    }
                }
                file = new File(fileDir +"/"+ timeStamp + ".pdf");
                String val = "Jordan Bank";
                String subTit = "\nJordan Bank Ltd.                                                                                                                                  VALID FOR SIX MONTHS ONLY";
                String details = "\n\n             (ISSUING BRANCH)                             BANK AND BRANCH CODE                      DD NO.                                        DATE:                    ";
                String details0 = "\n                   Hyderabad                                                       229      DE                                         6787654334                              "+dateFormat.format(date);
                String details1 = "\n\n             ON DEMAND PAY";
                String detailse = "                          "+payableFinal+"                                                                         ";
                //String details2 = "\n                         CBIT TRANSPORT*************";
                String details3 = "\n\n           RUPEES   ";
                String details4 = amountWordFinal;
                String detailsf = "   Rs. "+amountFinal+"/-";
                String details5 = "\n\n                                                                                                                                                                                                    FOR VALUE RECEIVED";
                String details6 = "\n\n\n\n                                                                                                                                                                                                     A/C No ("+pref.getString("accNo", null)+")";
                String details7 = "\n                                                                                                Payable AT PAR at ALL branches";
                Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
                Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL);
                Font normFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);
                Font detFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
                Document document = new Document();
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
                Rectangle one = new Rectangle(800, 360);
                Rectangle two = new Rectangle(700, 400);
                document.setPageSize(one);
                document.setMargins(20, 20, 20, 20);
                document.open();
                document.add(new Chunk(val, boldFont).setUnderline(0.1f, -2f));
                Chunk c = new Chunk(subTit, normFont);
                document.add(new Phrase(c));
                document.add(new Phrase(details, detFont));
                document.add(new Phrase(details0, detFont));
                Chunk c1 = new Chunk(detailse, detFont).setUnderline(0.1f, -2f);
                document.add(new Phrase(details1, normFont));
                document.add(c1);
                //document.add(new Phrase(details2, detFont));
                document.add(new Phrase(details3, subFont));
                Chunk c2 = new Chunk(details4, detFont).setUnderline(0.1f, -2f);
                document.add(c2);
                Chunk c3 = new Chunk(detailsf, detFont).setUnderline(0.1f, -2f);
                document.add(c3);
                document.add(new Phrase(details5, detFont));
                document.add(new Phrase(details6, detFont));
                document.add(new Phrase(details7, detFont));
        /*document.setPageSize(two);
        document.setMargins(20, 20, 20, 20);
        document.newPage();
        document.add(p);*/
                document.close();
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }
            catch (DocumentException e){
                e.printStackTrace();
            }
            finally {
                return "1";
            }
        }
    }
}
