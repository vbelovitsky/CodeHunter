package com.kotlin.vbel.codehunter;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class ImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);


        ImageView image = findViewById(R.id.testImage);

        String imageURI = getIntent().getStringExtra("imageURI");

        image.setImageBitmap(BitmapFactory.decodeFile(imageURI));

        ImageButton leftButton = findViewById(R.id.imageButtonLeft);

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Uri contentURI = Uri.parse(imageUri);

        System.gc();
    }



    public String text;
    public void getTextFromImage(View view, Uri contentURI) throws IOException {


        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if(!textRecognizer.isOperational()){
            Toast.makeText(getApplicationContext(), "could not get the Text", Toast.LENGTH_SHORT).show();
        }
        else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();

            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < items.size(); ++i){
                TextBlock myItem = items.valueAt(i);
                sb.append(myItem.getValue());
                sb.append("\n");
            }

            text = sb.toString() ;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        System.gc();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        System.gc();
    }

}
