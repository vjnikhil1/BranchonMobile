package com.example.nikhil.branchonmobile;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Registration extends AppCompatActivity {
    private Button register,upload;
    private RadioGroup rg;
    private RadioButton rb;
    private EditText name,lname,email,mobile,pan,address,password;
    private String img;
    private Uri photoURI;
    String mCurrentPhotoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        register = (Button) findViewById(R.id.button2);
        upload = (Button) findViewById(R.id.button3);
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected = rg.getCheckedRadioButtonId();
                rb = (RadioButton) findViewById(selected);
                //Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_LONG).show();
                name = (EditText) findViewById(R.id.editText5);
                lname = (EditText) findViewById(R.id.editText6);
                email = (EditText) findViewById(R.id.editText7);
                mobile = (EditText) findViewById(R.id.editText8);
                address = (EditText) findViewById(R.id.editText10);
                password = (EditText) findViewById(R.id.editText4);
                ParallelThread pt = new ParallelThread(Registration.this);
                pt.execute("register",name.getText().toString(),lname.getText().toString(),
                        email.getText().toString(),mobile.getText().toString(),rb.getText().toString(),
                        img,address.getText().toString(),password.getText().toString());
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                }
                if (photoFile != null) {
                    photoURI = FileProvider.getUriForFile(Registration.this,
                            "com.example.nikhil.branchonmobile",
                            photoFile);
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                }

                String pickTitle = "Select or take a new Picture"; // Or get from strings.xml
                Intent chooserIntent = Intent.createChooser(pickPhoto, pickTitle);
                chooserIntent.putExtra
                        (
                                Intent.EXTRA_INITIAL_INTENTS,
                                new Intent[] { takePhotoIntent }
                        );

                startActivityForResult(chooserIntent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if(requestCode==PICK_IMAGE_REQUEST)
        //Log.e("x",""+resultCode);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if(uri==null) {
                uri=photoURI;
            }
                //Log.e("Only cam", uri.toString());
                try {
                    Bitmap b = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                    ByteArrayOutputStream bo = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.JPEG, 50, bo);
                    byte[] ba = bo.toByteArray();
                    img = Base64.encodeToString(ba, Base64.DEFAULT);
                    TextView panStatus = (TextView) findViewById(R.id.textView);
                    panStatus.setText("Upload Success");
                    panStatus.setTextColor(Color.parseColor("#4CAF50"));
                    //Log.e("yo", img);
                    //ImageView iv = (ImageView) findViewById(R.id.imageView);
                    //iv.setImageBitmap(b);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /*else
                {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    ByteArrayOutputStream bo = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bo);
                    byte[] ba = bo.toByteArray();
                    img = Base64.encodeToString(ba, Base64.DEFAULT);
                    //Log.e("image",img);
                }*/
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
