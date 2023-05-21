package com.example.snapchat

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.util.*
import android.graphics.drawable.Drawable
import java.io.FileOutputStream
import java.lang.Exception


class CreateSnap : AppCompatActivity() {
    lateinit var newData: ByteArray
    val storage = Firebase.storage
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }
    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_snap)
        val snapImageView: ImageView?=findViewById(R.id.snapImageView)
        val chooseImageButton: Button=findViewById(R.id.chooseImageButton)
        val messageEditText: EditText?=findViewById(R.id.messageEditText)
        val sendSnap: Button=findViewById(R.id.sendSnapButton)
        snapImageView?.isDrawingCacheEnabled = true
        snapImageView?.buildDrawingCache()
        val bitmap = (snapImageView?.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()


        val getResult =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val value = it.data?.getStringExtra("input")
                    val selectedImage= it!!.data?.data
                    val bitmap=MediaStore.Images.Media.getBitmap(this.contentResolver,selectedImage)
                    if (snapImageView != null) {
                        snapImageView.setImageBitmap(bitmap)
                    }
                }
            }
        chooseImageButton.setOnClickListener(){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            } else {
                val intent= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                getResult.launch(intent)
            }

            }
        sendSnap.setOnClickListener(){
            // Get the data from an ImageView as bytes
            snapImageView?.isDrawingCacheEnabled = true
            snapImageView?.buildDrawingCache()
            val bitmap = (snapImageView.drawable as BitmapDrawable).bitmap

            var fileName: String? = "myImage" //no .png or .jpg needed

            try {
                val bytes = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                val fo: FileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
                fo.write(bytes.toByteArray())
                // remember close file output
                fo.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }


            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            println(data)
            newData=data
            println(newData)
            val intent=Intent(this,friendsActivity::class.java)
            intent.putExtra("Message",messageEditText?.text.toString())
//            intent.putExtra("bitmap",bitmap)
            startActivity(intent)
//            uploadTask.addOnFailureListener {
//                // Handle unsuccessful uploads
//                println(it)
//                Toast.makeText(this,"Your image was not uploaded",Toast.LENGTH_SHORT).show()
//            }.addOnSuccessListener { taskSnapshot ->
//                Toast.makeText(this,"Your image was uploaded",Toast.LENGTH_SHORT).show()
//
//                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
//                // ...
//            }
        }
    }
}