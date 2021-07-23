package com.lb.lawbook.profile.services

import android.R
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lb.lawbook.databinding.ActivityServiceDetailBinding
import com.lb.lawbook.pojos.Service
import kotlinx.android.synthetic.main.activity_service_detail.*

class ServiceDetailsActivity : AppCompatActivity() {
    private lateinit var serviceDetailViewModel: ServiceDetailViewModel
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance().currentUser

    private var arrayChecked: ArrayList<Boolean> = ArrayList()
    private lateinit var serviceEdit: Service

    private var locationSelected: String = ""
    private var serviceTypeSelected: String = ""
    private var docIdEdit: String = ""
    private lateinit var languagesSelected: ArrayList<String>

    private lateinit var mBinding: ActivityServiceDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityServiceDetailBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        docIdEdit = intent.extras?.get("docIdEdit").toString()

        mBinding.buttonServicesDetailsInfoBack.setOnClickListener {
            onBackPressed()
        }

        serviceDetailViewModel =
                ViewModelProvider(this).get(ServiceDetailViewModel::class.java)

        if (!docIdEdit.equals("")) {
            serviceDetailViewModel.readOneServiceData(docIdEdit).observe(this, Observer {
                serviceEdit = it
            })
        }
        serviceDetailViewModel.readServiceMetaData().observe(this, Observer {
            selectLocation(it.get("locations") as ArrayList<String>)
            selectLanguages(it.get("languages") as ArrayList<String>)
            selectServiceType(it.get("service_type") as ArrayList<String>)
        })

        mBinding.saveServiceDetailsBtn.setOnClickListener {
            insertServices()
        }
    }


    private fun selectLanguages(arrayLang: ArrayList<String>) {
        lateinit var dialog: AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Languages")
        languages_TextView.setOnClickListener {
            builder.setMultiChoiceItems(arrayLang.toTypedArray(), arrayChecked.toBooleanArray(), { dialog, which, isChecked ->
                arrayChecked[which] = isChecked
            })
            builder.setPositiveButton("OK") { _, _ ->
                lang_list_textView.text = ""
                languagesSelected = ArrayList<String>()
                for (i in 0 until arrayLang.size) {
                    val checked = arrayChecked[i]
                    if (checked) {
                        languagesSelected.add(arrayLang[i])
                        lang_list_textView.text = "${lang_list_textView.text}  ${arrayLang[i]} " + ", "
                    }
                }
            }
            dialog = builder.create()
            dialog.show()
        }
        for (i in 0..arrayLang.size) {
            arrayChecked.add(false)
        }
    }

    private fun selectLocation(arrayLocation: ArrayList<String>) {
        var adapterArray = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, arrayLocation)
        searchable_location_spinnerView.adapter = adapterArray
        searchable_location_spinnerView.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Nothing selected
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                locationSelected = parent?.getItemAtPosition(id.toInt()).toString()
                Toast.makeText(this@ServiceDetailsActivity, locationSelected, Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun selectServiceType(arrayServiceType: ArrayList<String>) {
        searchable_service_spinnerView.adapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, arrayServiceType)
        searchable_service_spinnerView.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Nothing selected
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                serviceTypeSelected = parent?.getItemAtPosition(id.toInt()).toString()
                Toast.makeText(this@ServiceDetailsActivity, serviceTypeSelected, Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun modeCheckBox(): MutableMap<String, Any> {
        val modeAmount: MutableMap<String, Any> = HashMap()
        if (mode_call_checkBox.isChecked) {
            modeAmount.put("call", amount_call_EditView.text.toString())
        }
        if (mode_call_visit_checkBox.isChecked) {
            modeAmount.put("call_visit", amount_call_visit_EditView.text.toString())
        }
        if (mode_visit_checkBox.isChecked) {
            modeAmount.put("visit", amount_visit_EditView.text.toString())
        }

        return modeAmount
    }


    fun insertServices() {
        val user: MutableMap<String, Any> = HashMap()
        user.put("uid", auth?.uid.toString())
        user.put("modes", modeCheckBox())
        user.put("location", locationSelected)
        user.put("languages", languagesSelected)
        user.put("service_type", serviceTypeSelected)
        user.put("exp", yr_exp_editView.text.toString())
        val documentRef = db.collection("service_points").document()
        documentRef.set(user)
    }
}