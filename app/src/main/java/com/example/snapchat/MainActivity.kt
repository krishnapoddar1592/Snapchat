package com.example.snapchat

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onBackPressed() {
    }
        public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            logIn();
        }
    }
    var emailEditText: EditText?=null
    var passEditText: EditText?=null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emailEditText=findViewById(R.id.emailTextView)
        passEditText=findViewById(R.id.passTextView)
        val button: Button=findViewById(R.id.button)
        // Initialize Firebase Auth
        auth = Firebase.auth

        button.setOnClickListener {
            auth.signInWithEmailAndPassword(emailEditText?.text.toString(), passEditText?.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Write a message to the database
                        logIn()
                    } else {
                        // If sign in fails, then sign up.
                        auth.createUserWithEmailAndPassword(emailEditText?.text.toString(), passEditText?.text.toString()).addOnCompleteListener(this){task->
                            println("yes")
                            if(task.isSuccessful){
                                val database = Firebase.database
                                task.result.user?.let { it1 -> FirebaseDatabase.getInstance().getReference().child("Users").child(it1.uid).child("email").setValue(emailEditText?.text.toString()) }
                                //add to database
                                logIn()
                            }
                            else{
                                Toast.makeText(this,"Please check the login id and password again",Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                }
        }

    }
    public fun logIn(){
        val intent=Intent(this,SnapActivity::class.java)
        intent.putExtra("UserID",auth.currentUser?.uid)
        startActivity(intent)
    }
}