package com.kotlin.vbel.codehunter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

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
        final String[] languages = new String[len];
        final String[] expansions = new String[len];

        for (int i = 0; i < len; i++){
            String[] data = languages_data[i].split("=");
            languages[i] = data[0];
            expansions[i] = data[1];
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, languages);
        final AutoCompleteTextView actv = findViewById(R.id.autoLanguage);
        actv.setThreshold(1);
        actv.setAdapter(adapter);


        ImageButton copyButton = findViewById(R.id.imageButtonCopy);
        ImageButton saveButton = findViewById(R.id.imageButtonSave);
        ImageButton sendButton = findViewById(R.id.imageButtonSend);

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String mainText = recognizedTextView.getText().toString();
                String label = "CodeHunter";
                ClipData clip = ClipData.newPlainText(label, mainText);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(TextActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mainText = recognizedTextView.getText().toString();

                //find expansion for file
                String langInput = actv.getText().toString();
                String expansion = ".txt";
                int index = Arrays.asList(languages).indexOf(langInput);

                //Kostyil
                if (!langInput.equals("")) {
                    if (index != -1) {
                        expansion = expansions[index];
                    } else if (langInput.toCharArray()[0] == '.' && langInput.toCharArray().length > 1) {
                        expansion = langInput;
                    }
                }

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String fileName = "Code_" + timeStamp + expansion;
                try {
                    File file = new File(TextActivity.this.getExternalFilesDir("Code"), fileName);
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(mainText);
                    fileWriter.close();

                    Toast.makeText(TextActivity.this, "Saved to " + file.getPath(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(TextActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
