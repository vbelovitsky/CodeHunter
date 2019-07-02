package com.kotlin.vbel.codehunter;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import pub.devrel.easypermissions.EasyPermissions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Temp extends AppCompatActivity {

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

        else if (requestCode == CAMERA && data != null && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            currentPhotoPath = selectedImage.getPath();
            galleryAddPic();

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



}
