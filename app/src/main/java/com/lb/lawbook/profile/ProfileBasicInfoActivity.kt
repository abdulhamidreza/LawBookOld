package com.lb.lawbook.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lb.lawbook.databinding.ActivityProfileBasicInfoBinding

class ProfileBasicInfoActivity : AppCompatActivity() {

    private lateinit var profileViewModel: ProfileViewModel
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance().currentUser
    private lateinit var mBinding: ActivityProfileBasicInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityProfileBasicInfoBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.buttonProfileBasicInfoBack.setOnClickListener {
            onBackPressed()
        }

        profileViewModel =
                ViewModelProvider(this).get(ProfileViewModel::class.java)

        profileViewModel.readDataOwnData().observe(this, Observer {
            if (it != null) {
                try {
                    mBinding.editTextUserName.setText(it.get("user_name")?.toString())
                    mBinding.editTextFirstName.setText(it.get("first")?.toString())
                    mBinding.editTextLastName.setText(it.get("last")?.toString())
                    mBinding.editTextEmail.setText(it.get("email")?.toString())
                    mBinding.editTextAge.setText(it.get("age")?.toString())
                    mBinding.textViewGender.text = it.get("gender")?.toString()

                } catch (e: Exception) {
                }
            }
        })


        mBinding.buttonBasicInfoUpdate.setOnClickListener {
            insertUserDocument()

        }
    }

    fun insertUserDocument() {
        val userName = mBinding.editTextUserName.text.toString()
        val firstName = mBinding.editTextFirstName.text.toString()
        val lastName = mBinding.editTextLastName.text.toString()
        val email = mBinding.editTextEmail.text.toString()
        val age = mBinding.editTextAge.text.toString()
        val gender = mBinding.textViewGender.text.toString()

        val basicInfo: MutableMap<String, Any> = HashMap()
        basicInfo.put("user_name", userName)
        basicInfo.put("number", auth?.phoneNumber.toString())
        basicInfo.put("first", firstName)
        basicInfo.put("last", lastName)
        basicInfo.put("email", email)
        basicInfo.put("age", age)
        basicInfo.put("gender", gender)
        val documentRef = db.collection("users").document(auth?.uid.toString())
        documentRef.set(basicInfo)
    }
}