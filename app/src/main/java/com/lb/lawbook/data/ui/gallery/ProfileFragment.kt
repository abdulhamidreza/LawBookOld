package com.lb.lawbook.data.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
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
        profileViewModel.readDataOwnData().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                try {
                    val name = it.get("name") as HashMap<String, Any>
                    name.let {
                        show_fname_txt.text = show_fname_txt.text.toString() + name.get("first")
                        show_mname_txt.text = show_mname_txt.text.toString() + name.get("middle")
                        show_lname_txt.text = show_lname_txt.text.toString() + name.get("last")
                    }
                    val career = it.get("career") as HashMap<String, Any>
                    career.let {
                        show_y_of_exp_txt.text =
                            show_y_of_exp_txt.text.toString() + career.get("y_of_exp")
                        show_field_txt.text = show_field_txt.text.toString() + career.get("field")
                        show_no_of_cases_txt.text =
                            show_no_of_cases_txt.text.toString() + career.get("no_of_cases")
                        show_digree_txt.text =
                            show_digree_txt.text.toString() + career.get("digree")
                        show_bar_txt.text = show_bar_txt.text.toString() + career.get("bar")
                        show_registration_no_txt.text =
                            show_registration_no_txt.text.toString() + career.get("registration_no")
                    }
                } catch (e: Exception) {
                }
            } else {
                scrollViewEdit.visibility = VISIBLE
                scrollViewShow.visibility = INVISIBLE
            }
        })

        root.edit_profile_btn.setOnClickListener {
            scrollViewEdit.visibility = VISIBLE
            scrollViewShow.visibility = INVISIBLE
        }

        root.cancel_profile_btn.setOnClickListener {
            //todo cancle edit
        }

        root.save_profile_btn.setOnClickListener {
            insertUserDocument(root)
        }
        return root
    }


    fun insertUserDocument(root: View) {
        val userName: MutableMap<String, Any> = HashMap()
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
        val addressList: ArrayList<Any> = ArrayList()
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