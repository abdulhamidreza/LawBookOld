package com.lb.lawbook.data.ui.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lb.lawbook.R
import com.lb.lawbook.databinding.FragmentMyProfileBinding

class MyProfileFragment : Fragment() {

    private lateinit var mBinding: FragmentMyProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentMyProfileBinding.inflate(layoutInflater, container, false)

        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        mBinding.textViewProfileBasicInfo.setOnClickListener {

        }

        mBinding.textViewProfileProfessionalInfo.setOnClickListener {

        }

        mBinding.textViewProfileMyAddressInfo.setOnClickListener {

        }

        mBinding.textViewProfileLogout.setOnClickListener {

        }


    }


}