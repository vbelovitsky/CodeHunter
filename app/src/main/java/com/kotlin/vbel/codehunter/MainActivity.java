package com.kotlin.vbel.codehunter;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.core.content.FileProvider;
import pub.devrel.easypermissions.EasyPermissions;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    private static int GALLERY = 2;
    private static int CAMERA = 3;

    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton cameraButton = findViewById(R.id.imageButtonCamera);
        ImageButton galleryButton = findViewById(R.id.imageButtonGallery);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageFromCamera();
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (!EasyPermissions.hasPermissions(MainActivity.this, galleryPermissions)) {
                    EasyPermissions.requestPermissions(MainActivity.this, "Access for storage",
                            101, galleryPermissions);
                }

                imageFromGallery();
            }
        });



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

        if (resultCode != RESULT_CANCELED){
            if (requestCode == GALLERY && data != null && resultCode == RESULT_OK){
                Uri selectedImage = data.getData();
                currentPhotoPath = selectedImage.getPath();
                currentPhotoPath = currentPhotoPath.replace("/raw/","");
            }

            else if (requestCode == CAMERA && data != null && resultCode == RESULT_OK){
                galleryAddPic();
            }

            Intent imageActivityIntent = new Intent(this, ImageActivity.class);
            imageActivityIntent.putExtra("imageURI", currentPhotoPath);
            startActivity(imageActivityIntent);

            System.gc();
        }

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

    @Override
    public void onPause() {
        super.onPause();
        System.gc();
    }

}
