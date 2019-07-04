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
            }
        });

    }

    public void getMLTextFromImage(Bitmap bitmap){
        final String ERROR_MESSAGE = "Error, no text found";
        String SAMSUNG_MESSAGE = "Samsung(((";

        if (bitmap != null) {
            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

            FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance()
                    .getOnDeviceTextRecognizer();

            textRecognizer.processImage(image)
                    .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                        @Override
                        public void onSuccess(FirebaseVisionText result) {
                            String recognizedText = result.getText();
                            if (recognizedText.equals("")) recognizedText = ERROR_MESSAGE;
                            intentToTextActivity(recognizedText);
                        }
                    })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    intentToTextActivity(ERROR_MESSAGE);
                                }
                            });
        }
        else{
            intentToTextActivity(SAMSUNG_MESSAGE);
        }
    }

    private void intentToTextActivity(String recognizedText){
        Intent textActivityIntent = new Intent(ImageActivity.this, TextActivity.class);
        textActivityIntent.putExtra("recognizedText", recognizedText);
        startActivity(textActivityIntent);
    }

}
