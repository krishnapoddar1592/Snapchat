package com.example.snapchat

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.lang.reflect.TypeVariable
import java.util.*
import kotlin.collections.ArrayList
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.firebase.storage.UploadTask

class friendsActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)
        val listView: ListView?=findViewById(R.id.listView)
        auth = Firebase.auth
        val currentUserId=auth.currentUser?.uid
        val users: ArrayList<String> = ArrayList()
        val users_uuid: ArrayList<String> = ArrayList()
        var adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,users)
        listView?.adapter=adapter
        FirebaseDatabase.getInstance().getReference().child("Users").addChildEventListener(object: ChildEventListener{
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val email =snapshot.child("email").value as String
                val id =snapshot.key as String
                users_uuid.add(id)
                users.add(email)
                adapter.notifyDataSetChanged()
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}


        })
        listView?.setOnItemClickListener { parent, view, position, id ->
            listView.isClickable=false
            // Get the selected item text from ListView
            val user_email = parent.getItemAtPosition(position) as String
            val user_id =users_uuid.get(position)
            val intent = getIntent();
            val message=intent.getStringExtra("Message")
            val bitmap = BitmapFactory.decodeStream(this.openFileInput("myImage"))
            val baos = ByteArrayOutputStream()
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            }
            val data = baos.toByteArray()

            FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Messages").child(currentUserId!!).child("Message").setValue(message)
            val uploadTask: UploadTask= FirebaseStorage.getInstance().reference.child("images").child(user_id).child(currentUserId).child("image.jpg").putBytes(data)
            var uri: Uri
            uploadTask.addOnSuccessListener {t ->
                t.metadata!!.reference!!.downloadUrl.addOnCompleteListener{task ->
                    uri = task.result!!
                    println(uri)
                    FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Messages").child(currentUserId).child("URL").setValue(uri.toString())
                    val intent2 =Intent(this,SnapActivity::class.java)
                    intent2.putExtra("UserID",currentUserId)
                    startActivity(intent2)
                }
            }
        }
    }
}