package com.example.nikhil.branchonmobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.abbyy.ocrsdk.Client;
import com.abbyy.ocrsdk.ProcessingSettings;
import com.abbyy.ocrsdk.Task;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by Nikhil on 18-02-2017.
 */

public class AsyncProcessTask extends AsyncTask<String, String, Boolean> {
    private String inputFile, outputFile;
    public AsyncProcessTask(Activity activity) {
        this.activity = activity;
        dialog = new ProgressDialog(activity);
    }

    private ProgressDialog dialog;
    /** application context. */
    private final Activity activity;

    protected void onPreExecute() {
        dialog.setMessage("Processing");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    protected void onPostExecute(Boolean result) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        try {
            StringBuffer contents = new StringBuffer();

            FileInputStream fis = activity.openFileInput(outputFile);
            try {
                Reader reader = new InputStreamReader(fis, "UTF-8");
                BufferedReader bufReader = new BufferedReader(reader);
                String text = null;
                while ((text = bufReader.readLine()) != null) {
                    contents.append(text).append(System.getProperty("line.separator"));
                }
                OCRParse op = new OCRParse(contents.toString());
                String[] output = op.getDetails();
                Log.e("ChequeFinal", output[0]+output[1]+output[2]+output[3]);
            } finally {
                fis.close();
            }
            //Log.e("Output", contents.toString());
        } catch (Exception e) {
        }
        //activity.updateResults(result);
    }

    @Override
    protected Boolean doInBackground(String... args) {

        inputFile = args[0];
        outputFile = args[1];

        try {
            Client restClient = new Client();

                    // To create an application and obtain a password,
                    // register at http://cloud.ocrsdk.com/Account/Register
                    // More info on getting your application id and password at
                    // http://ocrsdk.com/documentation/faq/#faq3

                    // Name of application you created
                    restClient.applicationId = "Branch on Mobile";
            // You should get e-mail from ABBYY Cloud OCR SDK service with the application password
            restClient.password = "GLHHNSkZ04GHeY+ohgCxAETm";

            publishProgress( "Uploading image...");

            String language = "English"; // Comma-separated list: Japanese,English or German,French,Spanish etc.

            ProcessingSettings processingSettings = new ProcessingSettings();
            processingSettings.setOutputFormat( ProcessingSettings.OutputFormat.txt );
            processingSettings.setLanguage(language);

            publishProgress("Uploading..");

            // If you want to process business cards, uncomment this
			/*
			BusCardSettings busCardSettings = new BusCardSettings();
			busCardSettings.setLanguage(language);
			busCardSettings.setOutputFormat(BusCardSettings.OutputFormat.xml);
			Task task = restClient.processBusinessCard(filePath, busCardSettings);
			*/
            Task task = restClient.processImage(inputFile, processingSettings);

            while( task.isTaskActive() ) {
                // Note: it's recommended that your application waits
                // at least 2 seconds before making the first getTaskStatus request
                // and also between such requests for the same task.
                // Making requests more often will not improve your application performance.
                // Note: if your application queues several files and waits for them
                // it's recommended that you use listFinishedTasks instead (which is described
                // at http://ocrsdk.com/documentation/apireference/listFinishedTasks/).

                Thread.sleep(5000);
                publishProgress( "Waiting.." );
                task = restClient.getTaskStatus(task.Id);
            }

            if( task.Status == Task.TaskStatus.Completed ) {
                publishProgress( "Downloading.." );
                FileOutputStream fos = activity.openFileOutput(outputFile, Context.MODE_PRIVATE);

                try {
                    restClient.downloadResult(task, fos);
                } finally {
                    fos.close();
                }

                publishProgress( "Ready" );
            } else if( task.Status == Task.TaskStatus.NotEnoughCredits ) {
                throw new Exception( "Not enough credits to process task. Add more pages to your application's account." );
            } else {
                throw new Exception( "Task failed" );
            }

            return true;
        } catch (Exception e) {
            final String message = "Error: " + e.getMessage();
            publishProgress( message);
            Log.e("Cheque", message);
            return false;
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        // TODO Auto-generated method stub
        String stage = values[0];
        dialog.setMessage(stage);
        // dialog.setProgress(values[0]);
    }
}
