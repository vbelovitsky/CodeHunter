package com.kotlin.vbel.codehunter

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
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
import pub.devrel.easypermissions.EasyPermissions
import android.Manifest.permission
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.Manifest.permission.READ_EXTERNAL_STORAGE



class MainActivity : AppCompatActivity() {

    internal val GALLERY = 2
    internal val CAMERA = 3


    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cameraButton = findViewById<ImageButton>(R.id.imageButtonCamera)
        val galleryButton = findViewById<ImageButton>(R.id.imageButtonGallery)

        cameraButton.setOnClickListener {
            val intent = Intent(this, ImageActivity::class.java)
            intent.putExtra("buttonClicked", CAMERA)
            startActivity(intent)
        }

        galleryButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ImageActivity::class.java)
            intent.putExtra("buttonClicked", GALLERY)
            startActivity(intent)
        })

        val galleryPermissions =
            arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (!EasyPermissions.hasPermissions(this, *galleryPermissions)) {
            EasyPermissions.requestPermissions(
                this, "Access for storage",
                101, *galleryPermissions
            )
        }


    }

}
