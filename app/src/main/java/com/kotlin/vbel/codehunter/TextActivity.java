package com.kotlin.vbel.codehunter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.algorithmia.APIException;
import com.algorithmia.AlgorithmException;
import com.algorithmia.Algorithmia;
import com.algorithmia.AlgorithmiaClient;
import com.algorithmia.algo.AlgoResponse;
import com.algorithmia.algo.Algorithm;
//import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.lang.Object;

public class TextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);


        final TextView recognizedTextView = findViewById(R.id.recognizedText);
        final String recognizedText = getIntent().getStringExtra("recognizedText");
        recognizedTextView.setText(recognizedText);

        AlgorithmiaClient client = Algorithmia.client("simHuy2KeDChHkrT9d6sCPeyZ/b1");
        Algorithm algo = client.algo("PetiteProgrammer/ProgrammingLanguageIdentification/0.1.3");
        AlgoResponse result = null;
        try {
            result = algo.pipe(recognizedText);
            TextView test = findViewById(R.id.testAlgo);
            test.setText(result.asString());
        } catch (APIException e) {
            e.printStackTrace();
        } catch (AlgorithmException e) {
            e.printStackTrace();
        }

        String[] languages_data = getResources().getStringArray(R.array.languages);
        int len = languages_data.length;
        final String[] languages = new String[len];
        final String[] expansions = new String[len];

        for (int i = 0; i < len; i++) {
            String[] data = languages_data[i].split("=");
            languages[i] = data[0];
            expansions[i] = data[1];
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, languages);
        final AutoCompleteTextView actv = findViewById(R.id.autoLanguage);
        actv.setThreshold(1);
        actv.setAdapter(adapter);


        //region Buttons
        ImageButton copyButton = findViewById(R.id.imageButtonCopy);
        ImageButton saveButton = findViewById(R.id.imageButtonSave);
        ImageButton sendButton = findViewById(R.id.imageButtonSend);

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                saveFile(recognizedText, actv, languages, expansions);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uriToImage = saveFile(recognizedText, actv, languages, expansions);

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
                shareIntent.setType("*/*");
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
            }
        });
    }

    private String saveFile(String recognizedText, AutoCompleteTextView actv, String[]languages, String[]expansions){

        //find expansion for file
        String langInput = actv.getText().toString();
        String expansion = ".txt";
        int index = Arrays.asList(languages).indexOf(langInput);

        //expansion validation
        if (!langInput.equals("")) {
            if (index != -1) {
                expansion = expansions[index];
            } else if (langInput.toCharArray()[0] == '.' && langInput.toCharArray().length > 1 && langInput.toCharArray().length <= 5) {
                expansion = langInput;
            }
        }

        //Create file and write recognized text in it
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "Code_" + timeStamp + expansion;
        final File file = new File(TextActivity.this.getExternalFilesDir("Code"), fileName);
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(recognizedText);
            fileWriter.close();

            Toast.makeText(TextActivity.this, "Saved to " + file.getPath(), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(TextActivity.this, "Error!", Toast.LENGTH_SHORT).show();
        }
        return file.getAbsolutePath();
    }


}


