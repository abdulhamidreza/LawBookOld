package com.lb.lawbook.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lb.lawbook.databinding.ActivityProfileAddressBinding

class ProfileAddressActivity : AppCompatActivity() {
    private lateinit var profileViewModel: ProfileViewModel
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance().currentUser
    private lateinit var mBinding: ActivityProfileAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityProfileAddressBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.buttonProfileAddressInfoBack.setOnClickListener {
            onBackPressed()
        }

        profileViewModel =
                ViewModelProvider(this).get(ProfileViewModel::class.java)

        profileViewModel.readDataOwnData().observe(this, Observer {
            if (it != null) {
                try {
                    mBinding.editTextProfileAddressHouseNo.setText(it.get("house_no").toString())
                    mBinding.editTextProfileAddressStreetName.setText(it.get("street")?.toString())
                    mBinding.editTextProfileAddressCity.setText(it.get("city")?.toString())
                    mBinding.editTextProfileAddressPinCode.setText(it.get("pin")?.toString())

                } catch (e: Exception) {
                }
            }
        })


        mBinding.buttonProfileAddressUpdate.setOnClickListener {
            insertUserDocument()

        }
    }

    fun insertUserDocument() {
        val houseNo = mBinding.editTextProfileAddressHouseNo.text
        val street = mBinding.editTextProfileAddressStreetName.text
        val city = mBinding.editTextProfileAddressCity.text
        val pin = mBinding.editTextProfileAddressPinCode.text

        val basicInfo: MutableMap<String, Any> = HashMap()
        basicInfo.put("house_no", houseNo!!)
        basicInfo.put("street", street!!)
        basicInfo.put("city", city!!)
        basicInfo.put("pin", pin!!)
        val documentRef = db.collection("users").document(auth?.uid.toString())
        documentRef.set(basicInfo)
    }
}