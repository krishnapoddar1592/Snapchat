package com.example.snapchat

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.FirebaseStorageKtxRegistrar
import com.google.firebase.storage.ktx.storage
import com.firebase.ui.storage.images.FirebaseImageLoader

import com.bumptech.glide.module.AppGlideModule
import com.google.android.gms.tasks.Task
import java.io.InputStream


class ShowSnap : AppCompatActivity() {
    @GlideModule
    class MyAppGlideModule : AppGlideModule() {
        fun registerComponents(context: Context?, registry: Registry) {
            // Register FirebaseImageLoader to handle StorageReference
            registry.append(
                StorageReference::class.java, InputStream::class.java,
                FirebaseImageLoader.Factory()
            )
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_snap)
        val intent =intent
        val userId=intent.getStringExtra("user")
        val senderId=intent.getStringExtra("sender")
        val imageView: ImageView=findViewById(R.id.snapImageView)
        val message: TextView=findViewById(R.id.textView)
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId.toString()).child("Messages").child(senderId.toString()).addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val text =snapshot.child("Message").value as String
                val url=snapshot.child("URL").value as String
                message.text=text
                Glide.with(applicationContext).load(url).into(imageView);

                FirebaseDatabase.getInstance().getReference().child("Users").child(userId.toString()).child("Messages").child(senderId.toString()).removeValue()
//                FirebaseDatabase.getInstance().getReference().child("Users").child(userId.toString()).child("Messages").child(senderId.toString()).child("URL").removeValue()
                FirebaseStorage.getInstance().getReference().child("images").child(userId.toString()).child(senderId.toString()).delete()


            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

//        onBackPressed(){}
//        message.setText(FirebaseDatabase.getInstance().getReference().child("Users").child(userId.toString()).child("Messages").child())
    }
}