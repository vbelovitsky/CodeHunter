package com.kotlin.vbel.codehunter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);


        ImageView image = findViewById(R.id.testImage);

        String imageURI = getIntent().getStringExtra("imageURI");

        image.setImageBitmap(BitmapFactory.decodeFile(imageURI));

    }


    //Uri contentURI = Uri.parse(imageURI);
//
    //public String text;
    //public void getTextFromImage(View view) throws IOException {
//
//
    //    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
    //    TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
    //    if(!textRecognizer.isOperational()){
    //        Toast.makeText(getApplicationContext(), "could not get the Text", Toast.LENGTH_SHORT).show();
    //    }
    //    else {
    //        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
//
    //        SparseArray<TextBlock> items = textRecognizer.detect(frame);
    //        StringBuilder sb = new StringBuilder();
    //        for(int i = 0; i < items.size(); ++i){
    //            TextBlock myItem = items.valueAt(i);
    //            sb.append(myItem.getValue());
    //            sb.append("\n");
    //        }
//
    //        text = sb.toString() ;
    //    }
    //}


}
