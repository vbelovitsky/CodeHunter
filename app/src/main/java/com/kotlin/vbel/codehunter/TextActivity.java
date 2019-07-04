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
import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.*;
import java.net.URLConnection;
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


        recognizeAlgo(recognizedText);


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
                saveFile(recognizedText, actv, languages, expansions, true);
            }
        });
        //TODO: 1 - УДАЛЕНИЕ
        //      2 - ТЕКСТ ОКОЛО КНОПОК
        //      3 - ПЕРЕРАБОТАТЬ TOAST ЧТОБЫ НЕ ПОКАЗЫВАЛ СОХРАНЕНИЕ : сделал, добавив boolean в safefile()
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] nameAndPath = saveFile(recognizedText, actv, languages, expansions, false);
                String UriIntent = nameAndPath[0];
                String fileName = nameAndPath[1];
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(UriIntent));
                shareIntent.setType("*/*");
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
            }
        });
    }


    private void recognizeAlgo(final String text) {
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

                                    JSONArray jsonArray = new JSONArray(result.asJsonString());
                                    //String[] arr = result.asJsonString().split("],[");

                                    TextView test = findViewById(R.id.testAlgo);
                                    test.setText(result.asJsonString());
                                    //тут парсим json и выводим куда нибудь резы
                                }

                            } catch (AlgorithmException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
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


