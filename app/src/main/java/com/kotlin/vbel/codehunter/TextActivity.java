package com.kotlin.vbel.codehunter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


public class TextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        final TextView recognizedTextView = findViewById(R.id.recognizedText);
        String recognizedText = getIntent().getStringExtra("recognizedText");
        recognizedTextView.setText(recognizedText);

        String[] languages_data = getResources().getStringArray(R.array.languages);
        int len = languages_data.length;
        String[] languages = new String[len];
        String[] expansions = new String[len];

        for (int i = 0; i < len; i++){
            String[] data = languages_data[i].split("=");
            languages[i] = data[0];
            expansions[i] = data[1];
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, languages);
        AutoCompleteTextView actv = findViewById(R.id.autoLanguage);
        actv.setThreshold(1);
        actv.setAdapter(adapter);


        ImageButton copyButton = findViewById(R.id.imageButtonCopy);
        ImageButton saveButton = findViewById(R.id.imageButtonSave);
        ImageButton sendButton = findViewById(R.id.imageButtonSend);

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String getstring = recognizedTextView.getText().toString();
                String label = "CodeHunter";
                ClipData clip = ClipData.newPlainText(label, getstring);
                clipboard.setPrimaryClip(clip);
            }
        });



    }
}
