package com.lb.lawbook.data.ui.services

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class ServiceDetailViewModel : ViewModel() {

    val db = FirebaseFirestore.getInstance()
    val TAG = "ServiceDetailViewModel"
    private val servicesMetaLiveData = MutableLiveData<MutableMap<String, Any>>().apply {
        db.collection("client_meta_data").document("service_meta")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        value = task.result?.data
                    } else {
                        Log.w(TAG, "Error getting document.", task.exception)
                    }
                }
    }

    fun readServiceMetaData(): MutableLiveData<MutableMap<String, Any>> {
        //It's point to document so we can get data fields
        return servicesMetaLiveData
    }

}