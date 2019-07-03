package com.kotlin.vbel.codehunter;


import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

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

        System.gc();
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
