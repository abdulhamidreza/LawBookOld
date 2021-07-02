package com.lb.lawbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.lb.lawbook.pojos.Address
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*


class MainActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    lateinit var userText: TextView
    lateinit var readText: TextView
    lateinit var signOutBtn: Button
    val TAG = "MainActivity"
    val auth = FirebaseAuth.getInstance().currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userText = findViewById(R.id.textViewUserDetails)
        readText = findViewById(R.id.textViewRead)
        signOutBtn = findViewById(R.id.buttonSignOut)

        if (auth != null) {
            userText.text = auth.phoneNumber
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
        }

        signOutBtn.setOnClickListener(View.OnClickListener {
            if (auth != null) {
                Firebase.auth.signOut()
                Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        })
    }

    fun insertUserDocument() {
        val userName: MutableMap<String, Any> =
            HashMap() //As a Map data type first, middle, and last name
        userName.put("first", fname_txt.text.toString())
        userName.put("middle", mname_txt.text.toString())
        userName.put("last", sname_txt.text.toString())

        val career: MutableMap<String, Any> = HashMap()
        career.put("y_of_exp", y_of_exp_txt.text.toString())
        career.put("field", field_txt.text.toString())
        career.put("no_of_cases", no_of_cases_txt.text.toString())
        career.put("digree", digree_txt.text.toString())
        career.put("bar", bar_txt.text.toString())
        career.put("registration_no", registration_no_txt.text.toString())

        val address = Address()
        val addressList: ArrayList<Any> = ArrayList()   //Array is not a good option, can not update individual field
        //HOME
        address.address_type = "house"
        address.house_no = house_no_txt_home.text.toString()
        address.street= street_txt_home.text.toString()
        address.city= city_txt_home.text.toString()
        address.pin= pin_txt_home.text.toString()
        addressList.add(address)
        //Office One
        address.address_type = "office1"
        address.house_no =  house_no_txt_off1.text.toString()
        address.street= street_txt_off1.text.toString()
        address.city= city_txt_off1.text.toString()
        address.pin=  pin_txt_off1.text.toString()
        addressList.add(address)
        //Office Two
        address.address_type = "office2"
        address.house_no =  house_no_txt_off2.text.toString()
        address.street= street_txt_off2.text.toString()
        address.city= city_txt_off2.text.toString()
        address.pin= pin_txt_off2.text.toString()
        addressList.add(address)
        val user: MutableMap<String, Any> = HashMap()
        user.put("number", auth?.phoneNumber.toString())
        user.put("born", age_txt.text.toString())
        user.put("name", userName)
        user.put("career", career)
        user.put("addresses", addressList)
        val documentRef = db.collection("users").document(auth?.uid.toString())
        documentRef.set(user)
    }

    fun readDataOwnData() {
        //It's point to document so we can get data fields
        db.collection("users").document(auth?.uid.toString())
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, " Lv1 => " + task.result?.data)
                } else {
                    Log.w(TAG, "Error getting document.", task.exception)
                }
            }

        //pointed to collection
        db.collection("users")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d(TAG, document.id + "Lv1 => " + document.data)
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }

    }

    fun insetToFB(view: View) {
        insertUserDocument()
    }

    fun readFromFB(view: View) {
        Log.d(TAG, "======================================================\n")
        readDataOwnData()
    }

    fun payNow(view: View) {
       // startActivity(Intent(this, RazorPayActivity::class.java))
    }

}