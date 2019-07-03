package com.kotlin.vbel.codehunter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import java.io.IOException;

public class ImageActivity extends Activity {

    private static String ERROR_MESSAGE = "Error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);


        ImageView image = findViewById(R.id.mainImage);

        final String imageURI = getIntent().getStringExtra("imageURI");

        image.setImageBitmap(BitmapFactory.decodeFile(imageURI));

        ImageButton leftButton = findViewById(R.id.imageButtonLeft);
        ImageButton checkButton = findViewById(R.id.imageButtonCheck);

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recognizedText = getTextFromImage(imageURI);
                Intent textActivityIntent = new Intent(ImageActivity.this, TextActivity.class);
                textActivityIntent.putExtra("recognizedText", recognizedText);
                startActivity(textActivityIntent);
            }
        });

    }

    public String getTextFromImage(String imageURI){

        Bitmap bitmap = BitmapFactory.decodeFile(imageURI);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if(!textRecognizer.isOperational()){
            Toast.makeText(getApplicationContext(), "Could no get the Text", Toast.LENGTH_SHORT).show();

            return ERROR_MESSAGE;
        }
        else{
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();

            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i< items.size(); i++){
                TextBlock myItem = items.valueAt(i);
                sb.append(myItem.getValue());
                sb.append("\n");
            }

            return sb.toString();
        }

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

    @Override
    public void onDestroy(){
        super.onDestroy();
        System.gc();
    }

}
