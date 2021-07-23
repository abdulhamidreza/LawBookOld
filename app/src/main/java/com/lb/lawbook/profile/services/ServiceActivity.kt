package com.lb.lawbook.profile.services

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lb.lawbook.databinding.ActivityServicesBinding
import com.lb.lawbook.pojos.Service
import kotlinx.android.synthetic.main.activity_services.*


class ServiceActivity : CustomButtonListener, AppCompatActivity(), ServiceDetailAdaptor.CustomButtonListener {
    private lateinit var profileViewModel: ServiceDetailViewModel
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance().currentUser
    private lateinit var mBinding: ActivityServicesBinding
    private lateinit var services: ArrayList<Service>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityServicesBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.buttonServicesInfoBack.setOnClickListener {
            onBackPressed()
        }

        mBinding.addNewServiceBtn.setOnClickListener {
            startActivity(
                    Intent(
                            this,
                            ServiceDetailsActivity::class.java
                    ).putExtra("docIdEdit", "")
            )
        }

        profileViewModel =
                ViewModelProvider(this).get(ServiceDetailViewModel::class.java)

        profileViewModel.readServicesData().observe(this, Observer {
            if (it != null) {
                services = it
                selectLocation()
            }
        })
    }

    private fun selectLocation() {
        //adding a layoutmanager
        var mang = LinearLayoutManager(this)
        mang.orientation = RecyclerView.VERTICAL
        recycleView_services.layoutManager = mang

        val adapter = ServiceDetailAdaptor(services)
        adapter.setCustomButtonListner(this)
        recycleView_services.adapter = adapter
    }

    override fun onButtonClickedListener(position: Int) {
        super.onButtonClickedListener(position)
        startActivity(
                Intent(
                        this,
                        ServiceDetailsActivity::class.java
                ).putExtra("docIdEdit", services.get(position).docId)
        )
    }

}

