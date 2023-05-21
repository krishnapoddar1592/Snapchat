package com.example.snapchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class SnapActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onBackPressed() {

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snap)
        val listView: ListView?=findViewById(R.id.listView)
        val snapSenders: ArrayList<String> = ArrayList()
        val users_uuid: ArrayList<String> = ArrayList()
        var adapter= ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,snapSenders)
        listView?.adapter=adapter
        auth = Firebase.auth
        val intent=getIntent()
        val userId=intent.getStringExtra("UserID")
        FirebaseDatabase.getInstance().reference.child("Users").child(userId.toString()).child("Messages").addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val snapSenderId = snapshot.key as String
                    users_uuid.add(snapSenderId)
                    FirebaseDatabase.getInstance().getReference().child("Users").child(snapSenderId)
                        .child("email").addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val snapSenderEmail = snapshot.value.toString()
                                snapSenders.add(snapSenderEmail)
                                adapter.notifyDataSetChanged()
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })


            }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val snapSenderId = snapshot.key as String
                    users_uuid.remove(snapSenderId)
                    FirebaseDatabase.getInstance().getReference().child("Users").child(snapSenderId)
                        .child("email").addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val snapSenderEmail = snapshot.value.toString()
                                snapSenders.remove(snapSenderEmail)
                                adapter.notifyDataSetChanged()
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })
                }
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}

        })

        listView?.setOnItemClickListener { parent, view, position, id ->
            val senderId = users_uuid[position]
            val intent3=Intent(this,ShowSnap::class.java)
            intent3.putExtra("sender",senderId)
            intent3.putExtra("user",userId)
            startActivity(intent3)
        }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater=menuInflater
        inflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.send_snap){
            val intent =Intent(this,CreateSnap::class.java)
            startActivity(intent)
        }
        else if(item.itemId==R.id.Logout){
            logout()
        }
        return super.onOptionsItemSelected(item)
    }
    fun logout(){
        auth.signOut()
        finish()
        val intent=Intent(this,MainActivity::class.java)
        intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}