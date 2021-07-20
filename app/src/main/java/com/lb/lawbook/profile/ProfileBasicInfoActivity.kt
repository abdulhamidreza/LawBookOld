package com.lb.lawbook.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lb.lawbook.databinding.ActivityProfileBasicInfoBinding

class ProfileBasicInfoActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityProfileBasicInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityProfileBasicInfoBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.buttonProfileBasicInfoBack.setOnClickListener {
            onBackPressed()
        }

        mBinding.buttonBasicInfoUpdate.setOnClickListener {
            val userName = mBinding.editTextUserName.text.toString()
            val firstName = mBinding.editTextFirstName.text.toString()
            val lastName = mBinding.editTextLastName.text.toString()
            val age = mBinding.editTextAge.text.toString()
            // todo update info to Firebase
        }

    }
}