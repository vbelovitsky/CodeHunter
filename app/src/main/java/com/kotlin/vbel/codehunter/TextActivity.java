package com.kotlin.vbel.codehunter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.algorithmia.APIException;
import com.algorithmia.AlgorithmException;
import com.algorithmia.algo.AlgoResponse;
import com.algorithmia.algo.Algorithm;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TextActivity extends AppCompatActivity {

    private final static String TAG = TextActivity.class.getSimpleName();

    public final static String RECOGNIZED_TEXT_KEY = "recognizedText";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        final TextView recognizedTextView = findViewById(R.id.recognizedText);
        final String recognizedText = getIntent().getStringExtra(RECOGNIZED_TEXT_KEY);
        recognizedTextView.setText(recognizedText);

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

        //important!
        recognizeAlgo(recognizedText, languages_data, actv);


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
                saveFile(recognizedText, actv, languages, expansions, true);
            }
        });
       sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, recognizedText);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
           }
        });
    }


    private void recognizeAlgo(final String text, final String[] languages_data, final AutoCompleteTextView actv) {
        Thread asyncThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Algorithm langDetect = App.algorithmiaClient.algo("PetiteProgrammer/ProgrammingLanguageIdentification/0.1.3");
                langDetect.setTimeout(300L, TimeUnit.MILLISECONDS); //optional
                try {
                    final AlgoResponse result = langDetect.pipe(text);
                    TextActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.d(TAG, result.asJsonString());
                                if(result.isSuccess()){

                                    String[] dataArray = result.asJsonString().replaceAll("\\[|\\]", "").split(",");

                                    String[] langArray = new String[3];
                                    Double[] chanceArray = new Double[3];
                                    for(int i = 0; i < 6; i++){
                                        if (i % 2 == 0)
                                            langArray[i/2] = capitalize(dataArray[i].replaceAll("\"", ""));
                                        else
                                            chanceArray[i/2] = Double.parseDouble(dataArray[i]);
                                    }

                                    //important!
                                    setLang(langArray, chanceArray, languages_data, actv);
                                }

                            } catch (AlgorithmException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (APIException e) {
                    Log.e(TAG, "recognizeAlgo()", e);
                }

            }
        });
        asyncThread.start();
    }


    public static String capitalize(String str)
    {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void setLang(String[] langArray, final Double[] chanceArray, String[] languages_data, AutoCompleteTextView actv){
        TextView lang1 = findViewById(R.id.lang1);
        TextView lang2 = findViewById(R.id.lang2);
        TextView lang3 = findViewById(R.id.lang3);

        if (Arrays.asList(languages_data).contains(langArray[0])) actv.setText(langArray[0]);
        else if(Arrays.asList(languages_data).contains(langArray[1])) actv.setText(langArray[1]);
        else if(Arrays.asList(languages_data).contains(langArray[2])) actv.setText(langArray[2]);

        lang1.setText(langArray[0]);
        lang2.setText(langArray[1]);
        lang3.setText(langArray[2]);

        lang1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chanceInfo = "Probability: " + chanceArray[0].toString();
                Toast.makeText(TextActivity.this, chanceInfo, Toast.LENGTH_SHORT).show();
            }
        });
        lang2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chanceInfo = "Probability: " + chanceArray[1].toString();
                Toast.makeText(TextActivity.this, chanceInfo, Toast.LENGTH_SHORT).show();
            }
        });
        lang3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chanceInfo = "Probability: " + chanceArray[2].toString();
                Toast.makeText(TextActivity.this, chanceInfo, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String[] saveFile(String recognizedText, AutoCompleteTextView actv, String[] languages, String[] expansions, boolean bula) {

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
        String[] ret = new String[2];
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(recognizedText);
            fileWriter.close();
            if(bula)
                Toast.makeText(TextActivity.this, "Saved to " + file.getPath(), Toast.LENGTH_SHORT).show();
            ret[0] = file.getAbsolutePath();
            ret[1] = file.getName();
            return ret;
        } catch (Exception e) {
            if(bula)
                Toast.makeText(TextActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            ret[0] = "";
            ret[1] = "";
            return ret;
        }

    }

}


