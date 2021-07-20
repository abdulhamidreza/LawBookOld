package com.lb.lawbook.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance().currentUser
    val TAG = "ProfileViewModel"
    private val userLiveData = MutableLiveData<MutableMap<String, Any>>().apply {
        db.collection("users").document(auth?.uid.toString())
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, " Lv1 => " + task.result?.data)
                        value = task.result?.data
                    } else {
                        Log.w(TAG, "Error getting document.", task.exception)
                    }
                }
    }

    fun readDataOwnData(): MutableLiveData<MutableMap<String, Any>> {
        //It's point to document so we can get data fields
        return userLiveData
    }

}