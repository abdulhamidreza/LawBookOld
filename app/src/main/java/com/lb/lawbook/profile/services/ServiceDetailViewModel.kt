package com.lb.lawbook.profile.services

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lb.lawbook.pojos.Service


class ServiceDetailViewModel : ViewModel() {

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance().currentUser
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

    private val servicesLiveData = MutableLiveData<ArrayList<Service>>().apply {
        db.collection("service_points")
                .whereEqualTo("uid", auth?.uid)
                .get()
                .addOnCompleteListener { task ->
                    var result = ArrayList<Service>()
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            var add = Service()
                            add.exp = document.data.get("exp").toString()
                            add.lang = document.data.get("languages") as List<String>
                            add.locations = document.data.get("location").toString()
                            add.modes = document.data.get("modes") as HashMap<*, *>
                            add.service_type = document.data.get("service_type").toString()
                            add.docId = document.id
                            result.add(add)
                        }
                        value = result
                    } else {
                        Log.w(TAG, "Error getting document.", task.exception)
                    }
                }
    }

    fun readServicesData(): MutableLiveData<ArrayList<Service>> {
        return servicesLiveData
    }


    fun readOneServiceData(docId: String): MutableLiveData<Service> {
        return MutableLiveData<Service>().apply {
            db.collection("service_points")
                    .document(docId)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            var add = Service()
                            add.exp = task.result?.data?.get("exp").toString()
                            add.lang = task.result?.data?.get("languages") as List<String>
                            add.locations = task.result?.data?.get("location").toString()
                            add.modes = task.result?.data?.get("modes") as HashMap<*, *>
                            add.service_type = task.result?.data?.get("service_type").toString()
                            add.docId = task.result?.id
                            value = add
                        } else {
                            Log.w(TAG, "Error getting document.", task.exception)
                        }
                    }
        }
    }

}