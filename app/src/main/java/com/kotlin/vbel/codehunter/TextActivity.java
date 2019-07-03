package com.kotlin.vbel.codehunter;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class TextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        TextView recognizedTextView = findViewById(R.id.recognizedText);
        String recognizedText = getIntent().getStringExtra("recognizedText");

        recognizedTextView.setText(recognizedText);


    }
}
