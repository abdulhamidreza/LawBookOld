package com.lb.lawbook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.lb.lawbook.databinding.ActivityHomeBinding
import com.lb.lawbook.profile.ProfileActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var mHomeBinding: ActivityHomeBinding
    val auth = FirebaseAuth.getInstance().currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(mHomeBinding.root)
        mHomeBinding.layoutHomeProfile.buttonHomeBottomProfile.setOnClickListener {
            startActivity(
                    Intent(
                            this@HomeActivity, ProfileActivity::class.java
                    )
            )
        }

    }

    override fun onStart() {
        super.onStart()
        if (auth == null) {
            Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}