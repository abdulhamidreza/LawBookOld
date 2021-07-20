package com.lb.lawbook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lb.lawbook.databinding.ActivityHomeBinding
import com.lb.lawbook.profile.ProfileActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var mHomeBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(mHomeBinding.root)
        mHomeBinding.layoutHomeProfile.buttonHomeBottomProfile.setOnClickListener {
            startActivity(
                Intent(
                    this@HomeActivity ,  ProfileActivity::class.java
                )
            )
        }

    }

}