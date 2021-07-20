package com.lb.lawbook.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lb.lawbook.R
import com.lb.lawbook.databinding.ActivityProfileProfessionalBinding

class ProfileProfessionalActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityProfileProfessionalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityProfileProfessionalBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.buttonProfileProfessionalInfoBack.setOnClickListener {
            onBackPressed()
        }

    }
}