package com.lb.lawbook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    lateinit var userText: TextView
    lateinit var readText: TextView
    lateinit var signOutBtn: Button
    val TAG = "MainActivity"
    val auth = FirebaseAuth.getInstance().currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userText = findViewById(R.id.textViewUserDetails)
        readText = findViewById(R.id.textViewRead)
        signOutBtn = findViewById(R.id.buttonSignOut)

        if (auth != null) {
            userText.text = auth.phoneNumber
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
        }

        signOutBtn.setOnClickListener(View.OnClickListener {
            if (auth != null) {
                Firebase.auth.signOut()
                Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        })
    }
}