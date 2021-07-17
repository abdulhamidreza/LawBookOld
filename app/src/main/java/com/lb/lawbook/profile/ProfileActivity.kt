package com.lb.lawbook.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lb.lawbook.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.buttonProfileBack.setOnClickListener {
            onBackPressed()
        }

        mBinding.textViewProfileBasicInfo.setOnClickListener {
            startActivity(
                Intent(
                    this@ProfileActivity,
                    ProfileBasicInfoActivity::class.java
                )
            )
        }

        mBinding.textViewProfileProfessionalInfo.setOnClickListener {
            startActivity(
                Intent(
                    this@ProfileActivity,
                    ProfileProfessionalActivity::class.java
                )
            )
        }

        mBinding.textViewProfileMyAddressInfo.setOnClickListener {
            startActivity(
                Intent(
                    this@ProfileActivity,
                    ProfileAddressActivity::class.java
                )
            )
        }


    }
}