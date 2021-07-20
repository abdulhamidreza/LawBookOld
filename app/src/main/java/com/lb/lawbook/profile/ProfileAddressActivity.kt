package com.lb.lawbook.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lb.lawbook.R
import com.lb.lawbook.databinding.ActivityProfileAddressBinding

class ProfileAddressActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityProfileAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityProfileAddressBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.buttonProfileAddressInfoBack.setOnClickListener {
            onBackPressed()
        }
    }
}