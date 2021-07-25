package com.lb.lawbook.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lb.lawbook.R
import com.lb.lawbook.databinding.ActivityProfileBasicInfoBinding
import java.util.*
import kotlin.collections.HashMap


class ProfileBasicInfoActivity : AppCompatActivity() {


    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    private var gender = ""
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
                    mBinding.editTextDob.setText(it.get("dob")?.toString())
                    gender = it.get("gender")?.toString()!!
                    if (gender.equals("male")) mBinding.genderRDG.check(R.id.radioButton_profile_gender_male)
                    if (gender.equals("female")) mBinding.genderRDG.check(R.id.radioButton_profile_gender_female)
                    if (gender.equals("others")) mBinding.genderRDG.check(R.id.radioButton_profile_gender_others)

                } catch (e: Exception) {
                }
            }
        })


        mBinding.buttonBasicInfoUpdate.setOnClickListener {
            insertUserDocument()

        }
        pickDob()
        setGender()
    }

    private fun setGender() {
        mBinding.genderRDG.setOnCheckedChangeListener { radioGroup, id ->
            if (id == R.id.radioButton_profile_gender_male) gender = "male"
            if (id == R.id.radioButton_profile_gender_female) gender = "female"
            if (id == R.id.radioButton_profile_gender_others) gender = "others"
        }

    }

    fun insertUserDocument() {
        val userName = mBinding.editTextUserName.text.toString()
        val firstName = mBinding.editTextFirstName.text.toString()
        val lastName = mBinding.editTextLastName.text.toString()
        val email = mBinding.editTextEmail.text.toString()
        val dob = mBinding.editTextDob.text.toString()

        val basicInfo: MutableMap<String, Any> = HashMap()
        basicInfo.put("user_name", userName)
        basicInfo.put("number", auth?.phoneNumber.toString())
        basicInfo.put("first", firstName)
        basicInfo.put("last", lastName)
        basicInfo.put("email", email)
        basicInfo.put("dob", dob)
        basicInfo.put("gender", gender)
        val documentRef = db.collection("users").document(auth?.uid.toString())
        documentRef.set(basicInfo)
    }


    fun pickDob() {
        mBinding.buttonDob.setOnClickListener(View.OnClickListener {
            // Get Current Date
            // Get Current Date
            val c: Calendar = Calendar.getInstance()
            c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 21)
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                    this,
                    { view, year, monthOfYear, dayOfMonth -> mBinding.editTextDob.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) },
                    mYear,
                    mMonth,
                    mDay
            )

            datePickerDialog.datePicker.maxDate = c.timeInMillis
            datePickerDialog.show()
        })
    }


}