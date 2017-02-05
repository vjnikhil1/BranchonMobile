package com.example.nikhil.branchonmobile;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Registration extends AppCompatActivity {
    private Button register,upload;
    private RadioGroup rg;
    private RadioButton rb;
    private EditText name,lname,email,mobile,pan,address,acctype;
    private String img;
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
                ParallelThread pt = new ParallelThread(Registration.this);
                pt.execute("register",name.getText().toString(),lname.getText().toString(),
                        email.getText().toString(),mobile.getText().toString(),rb.getText().toString(),
                        img,address.getText().toString());
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
            if(uri!=null) {
                //Log.e("Only cam", uri.toString());
                try {
                    Bitmap b = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                    ByteArrayOutputStream bo = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.JPEG, 70, bo);
                    byte[] ba = bo.toByteArray();
                    img = Base64.encodeToString(ba, Base64.DEFAULT);
                    //Log.e("yo", img);
                    //ImageView iv = (ImageView) findViewById(R.id.imageView);
                    //iv.setImageBitmap(b);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
                else
                {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    ByteArrayOutputStream bo = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bo);
                    byte[] ba = bo.toByteArray();
                    img = Base64.encodeToString(ba, Base64.DEFAULT);
                    //Log.e("image",img);
                }
        }
    }
}
