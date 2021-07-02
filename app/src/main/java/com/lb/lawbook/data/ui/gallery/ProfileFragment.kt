package com.lb.lawbook.data.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lb.lawbook.R
import com.lb.lawbook.pojos.Address
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        profileViewModel.text.observe(viewLifecycleOwner, Observer {
        })

        root.save_profile_btn.setOnClickListener { insertUserDocument(root) }
        return root
    }


    fun insertUserDocument(root: View) {
        val userName: MutableMap<String, Any> =
            HashMap() //As a Map data type first, middle, and last name
        userName.put("first", root.fname_txt.text.toString())
        userName.put("middle", root.mname_txt.text.toString())
        userName.put("last", root.sname_txt.text.toString())

        val career: MutableMap<String, Any> = HashMap()
        career.put("y_of_exp", root.y_of_exp_txt.text.toString())
        career.put("field", root.field_txt.text.toString())
        career.put("no_of_cases", root.no_of_cases_txt.text.toString())
        career.put("digree", root.digree_txt.text.toString())
        career.put("bar", root.bar_txt.text.toString())
        career.put("registration_no", root.registration_no_txt.text.toString())

        val address = Address()
        val addressList: ArrayList<Any> =
            ArrayList()   //Array is not a good option, can not update individual field
        //HOME
        address.address_type = "house"
        address.house_no = root.house_no_txt_home.text.toString()
        address.street = root.street_txt_home.text.toString()
        address.city = root.city_txt_home.text.toString()
        address.pin = root.pin_txt_home.text.toString()
        addressList.add(address)
        //Office One
        address.address_type = "office1"
        address.house_no = root.house_no_txt_off1.text.toString()
        address.street = root.street_txt_off1.text.toString()
        address.city = root.city_txt_off1.text.toString()
        address.pin = root.pin_txt_off1.text.toString()
        addressList.add(address)
        //Office Two
        address.address_type = "office2"
        address.house_no = root.house_no_txt_off2.text.toString()
        address.street = root.street_txt_off2.text.toString()
        address.city = root.city_txt_off2.text.toString()
        address.pin = root.pin_txt_off2.text.toString()
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

}