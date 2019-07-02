package com.kotlin.vbel.codehunter

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val cameraButton = findViewById<ImageButton>(R.id.imageButtonCamera)
        val galleryButton = findViewById<ImageButton>(R.id.imageButtonGallery)

        //cameraButton.setOnClickListener({
        //    val REQUEST_IMAGE_CAPTURE = 1
        //
        //    fun dispatchTakePictureIntent() {
        //        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
        //            takePictureIntent.resolveActivity(packageManager)?.also {
        //                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        //            }
        //        }
        //    }
        //})


        //region: Gallery
        galleryButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, Gallery::class.java)
            startActivity(intent)
        })
        //endregion
    }
}
