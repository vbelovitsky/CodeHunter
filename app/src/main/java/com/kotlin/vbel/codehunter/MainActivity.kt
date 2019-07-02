package com.kotlin.vbel.codehunter

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.view.View
import android.widget.ImageButton
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    internal val RESULT_GALLERY = 0
    internal val RESULT_CAMERA = 1


    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cameraButton = findViewById<ImageButton>(R.id.imageButtonCamera)
        val galleryButton = findViewById<ImageButton>(R.id.imageButtonGallery)

        cameraButton.setOnClickListener {
            val intent = Intent(this, ImageActivity::class.java)
            intent.putExtra("buttonClicked", RESULT_CAMERA)
            startActivity(intent)
        }

        galleryButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ImageActivity::class.java)
            intent.putExtra("buttonClicked", RESULT_GALLERY)
            startActivity(intent)
        })

    }

}
