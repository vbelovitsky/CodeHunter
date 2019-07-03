package com.kotlin.vbel.codehunter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

public class ImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);


        ImageView image = findViewById(R.id.mainImage);

        final String imageURI = getIntent().getStringExtra("imageURI");
        final Bitmap bitmap = BitmapFactory.decodeFile(imageURI);
        image.setImageBitmap(bitmap);

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
                getMLTextFromImage(bitmap);
                //Intent textActivityIntent = new Intent(ImageActivity.this, TextActivity.class);
                //textActivityIntent.putExtra("recognizedText", recognizedText);
                //startActivity(textActivityIntent);
            }
        });

    }

    public String getTextFromImage(String imageURI){
        String ERROR_MESSAGE = "Error";

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

            for(int i = 0; i < items.size(); i++){
                TextBlock myItem = items.valueAt(i);
                sb.append(myItem.getValue());
                sb.append("\n");
            }

            String recognizedText = sb.toString();
            if (recognizedText.equals("")){recognizedText = ERROR_MESSAGE;}
            return recognizedText;
        }
    }



    public void getMLTextFromImage(Bitmap bitmap){
        final String ERROR_MESSAGE = "Error, no text found";

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();

        textRecognizer.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText result) {
                        String recognizedText = result.getText();
                        if (recognizedText.equals("")) recognizedText = ERROR_MESSAGE;
                        Intent textActivityIntent = new Intent(ImageActivity.this, TextActivity.class);
                        textActivityIntent.putExtra("recognizedText", recognizedText);
                        startActivity(textActivityIntent);
                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Intent textActivityIntent = new Intent(ImageActivity.this, TextActivity.class);
                                textActivityIntent.putExtra("recognizedText", ERROR_MESSAGE);
                                startActivity(textActivityIntent);
                            }
                        });
    }



}
