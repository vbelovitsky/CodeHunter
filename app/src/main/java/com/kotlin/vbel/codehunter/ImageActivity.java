package com.kotlin.vbel.codehunter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
<<<<<<< HEAD
import android.database.Cursor;
=======
import android.graphics.Bitmap;
>>>>>>> 9d435e94720d579c11ca7ad5e7e2a1bef9a01055
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
<<<<<<< HEAD
import android.widget.ImageView;
=======
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
>>>>>>> 9d435e94720d579c11ca7ad5e7e2a1bef9a01055
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageActivity extends Activity {

    private static int GALLERY = 2;
    private static int CAMERA = 3;

    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        TextView test = findViewById(R.id.test);
        ImageView image = findViewById(R.id.testImage);

        int buttonClicked = getIntent().getIntExtra("buttonClicked", 0);

        if (buttonClicked == CAMERA){
            imageFromCamera();
        }
        else if(buttonClicked == GALLERY){
            imageFromGallery();
        }
    }

<<<<<<< HEAD
=======
    Uri contentURI;

>>>>>>> 9d435e94720d579c11ca7ad5e7e2a1bef9a01055
    private void imageFromCamera() {
        dispatchTakePictureIntent();
    }

    public void imageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY && data != null && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            currentPhotoPath = selectedImage.getPath();
            currentPhotoPath = currentPhotoPath.replace("/raw/","");
        }
<<<<<<< HEAD
        else if (requestCode == CAMERA && data != null && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            currentPhotoPath = selectedImage.getPath();
            galleryAddPic();
=======
        if (requestCode == GALLERY ){
            if (data != null) {
                contentURI = data.getData();
                currentPhotoPath = contentURI.getPath();
            }
>>>>>>> 9d435e94720d579c11ca7ad5e7e2a1bef9a01055
        }

        TextView test = findViewById(R.id.test);
        test.setText(currentPhotoPath);

        ImageView image = findViewById(R.id.testImage);
        image.setImageBitmap(BitmapFactory.decodeFile(currentPhotoPath));

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                return;
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA);
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

<<<<<<< HEAD
=======
    public String text;
    public void getTextFromImage(View view) throws IOException {


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

>>>>>>> 9d435e94720d579c11ca7ad5e7e2a1bef9a01055
}
