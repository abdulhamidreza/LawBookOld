package com.lb.lawbook.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lb.lawbook.databinding.ActivityProfileProfessionalBinding

class ProfileProfessionalActivity : AppCompatActivity() {

    private lateinit var profileViewModel: ProfileViewModel
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance().currentUser
    private lateinit var mBinding: ActivityProfileProfessionalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityProfileProfessionalBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.buttonProfileProfessionalInfoBack.setOnClickListener {
            onBackPressed()
        }

        profileViewModel =
                ViewModelProvider(this).get(ProfileViewModel::class.java)

        profileViewModel.readDataOwnData().observe(this, Observer {
            if (it != null) {
                try {
                    mBinding.editTextProfileYearsExperience.setText(it.get("yr_of_exp")?.toString())
                    mBinding.editTextExpertiseField.setText(it.get("expertise_field")?.toString())
                    mBinding.editTextProfileNoCases.setText(it.get("no_of_cases")?.toString())
                    mBinding.editTextProfileDegree.setText(it.get("degree")?.toString())
                    mBinding.editTextProfileBarName.setText(it.get("bar_name")?.toString())
                    mBinding.editTextProfileBarRegistrationNo.setText(it.get("bar_reg_no")?.toString())

                } catch (e: Exception) {
                }
            }
        })


        mBinding.buttonProfileProfessionalInfoUpdate.setOnClickListener {
            insertUserDocument()

        }
    }

    fun insertUserDocument() {
        val yearOfExp = mBinding.editTextProfileYearsExperience.text.toString()
        val expFiled = mBinding.editTextExpertiseField.text.toString()
        val noCases = mBinding.editTextProfileNoCases.text.toString()
        val degree = mBinding.editTextProfileDegree.text.toString()
        val barName = mBinding.editTextProfileBarName.text.toString()
        val barRegNo = mBinding.editTextProfileBarRegistrationNo.text.toString()

        val basicInfo: MutableMap<String, Any> = HashMap()
        basicInfo.put("yr_of_exp", yearOfExp)
        basicInfo.put("expertise_field", expFiled)
        basicInfo.put("no_of_cases", noCases)
        basicInfo.put("degree", degree)
        basicInfo.put("bar_name", barName)
        basicInfo.put("bar_reg_no", barRegNo)
        val documentRef = db.collection("users").document(auth?.uid.toString())
        documentRef.set(basicInfo)
    }
}