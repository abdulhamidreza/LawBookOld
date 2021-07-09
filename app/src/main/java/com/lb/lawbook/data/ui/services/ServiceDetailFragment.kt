package com.lb.lawbook.data.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lb.lawbook.R
import kotlinx.android.synthetic.main.fragment_service_detail.*


class ServiceDetailFragment : Fragment() {

    private var arrayChecked: ArrayList<Boolean> = ArrayList()
    private lateinit var locationSpinner: Spinner
    private lateinit var serviceTypeSpinner: Spinner

    private var locationSelected: String = ""
    private var serviceTypeSelected: String = ""
    private lateinit var languagesSelected: ArrayList<String>

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance().currentUser

    private lateinit var serviceDetailViewModel: ServiceDetailViewModel


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationSpinner = view.findViewById(R.id.searchable_location_spinnerView)
        serviceTypeSpinner = view.findViewById(R.id.searchable_service_spinnerView)

        serviceDetailViewModel = ViewModelProvider(this).get(ServiceDetailViewModel::class.java)
        serviceDetailViewModel.readServiceMetaData().observe(viewLifecycleOwner, Observer {
            selectLocation(it.get("locations") as ArrayList<String>)
            selectLanguages(it.get("languages") as ArrayList<String>)
            selectServiceType(it.get("service_type") as ArrayList<String>)
        })

        save_service_details_btn.setOnClickListener({ insertServices() })


    }

    // Method to show an alert dialog with multiple choice list items
    private fun showDialog(arrayLang: Array<String>) {


    }

    private fun selectLanguages(arrayLang: ArrayList<String>) {
        lateinit var dialog: AlertDialog
        val builder = AlertDialog.Builder(requireContext())
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
        locationSpinner.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, arrayLocation)
        locationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Nothing selected
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                locationSelected = parent?.getItemAtPosition(id.toInt()).toString()
                Toast.makeText(context, locationSelected, Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun selectServiceType(arrayServiceType: ArrayList<String>) {
        serviceTypeSpinner.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, arrayServiceType)
        serviceTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Nothing selected
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                serviceTypeSelected = parent?.getItemAtPosition(id.toInt()).toString()
                Toast.makeText(context, serviceTypeSelected, Toast.LENGTH_LONG).show()
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
        user.put("modes", modeCheckBox())
        user.put("location", locationSelected)
        user.put("languages", languagesSelected)
        user.put("service_type", serviceTypeSelected)
        user.put("yr_of_exp", yr_exp_editView.text.toString())
        val documentRef = db.collection("service_points").document(auth?.uid.toString())
        documentRef.set(user)
    }

}