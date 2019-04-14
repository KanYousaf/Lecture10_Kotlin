package com.example.kanwal_laptop.lecture10_kotlin

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    var currentPath: String? = null
    val CAMERA_REQCODE: Int = 1234
    val SELECT_PICTURE : Int = 4567

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraBtn.setOnClickListener {
            dispatchCameraIntent()
        }

        galleryBtn.setOnClickListener {
            dispatchGalleryIntent()
        }
    }

    fun dispatchCameraIntent() {
        val camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (camIntent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImage()

            } catch (e: IOException) {
                e.printStackTrace()
            }
            if(photoFile!=null){
                // Log.d(TAG, "photoFile=${photoFile.absolutePath}")
                // /storage/emulated/0/Android/data/com.example.kanwal_laptop.lecture10_kotlin/files/Pictures/JPG_20190413_175826_1464495388.jpg
                val authorties = packageName + ".fileProvider"
                val photoURI = FileProvider.getUriForFile(this, authorties, photoFile)
                camIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(camIntent, CAMERA_REQCODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CAMERA_REQCODE && resultCode == Activity.RESULT_OK){
            try{
                val file = File(currentPath)
                val uri = Uri.fromFile(file)
                capturedImageViewer.setImageURI(uri)
            }catch (e : IOException){
                e.printStackTrace()
            }
        }

        if(requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK){
            try{
                val uri = data!!.data
                capturedImageViewer.setImageURI(uri)
            }catch (e : IOException){
                e.printStackTrace()
            }
        }
    }

    fun createImage(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPG_" + timeStamp + "_"

        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val finalImage = File.createTempFile(imageFileName, ".jpg", storageDir)
        // Save a file path for use with ACTION_VIEW intents
        currentPath = finalImage.absolutePath
        return finalImage
    }


    fun dispatchGalleryIntent(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_PICTURE)
    }


    fun onNextButtonClicked(view: View){
        val databaseIntent = Intent(this, SQLDatabaseActivity :: class.java)
        startActivity(databaseIntent)
    }
}
