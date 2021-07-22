package com.lb.lawbook.profile.services

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lb.lawbook.R
import com.lb.lawbook.pojos.Service
import kotlinx.android.synthetic.main.services_list_item.view.*


class ServiceDetailAdaptor(val userList: ArrayList<Service>) : RecyclerView.Adapter<ServiceDetailAdaptor.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceDetailAdaptor.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.services_list_item, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ServiceDetailAdaptor.ViewHolder, position: Int) {
        holder.bindItems(userList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: Service) {
            itemView.item_service_id.text = itemView.item_service_id.text.toString() + user.docId
            itemView.item_service_name.text = itemView.item_service_name.text.toString() + user.service_type
            itemView.item_service_locations.text = itemView.item_service_locations.text.toString() + user.location
        }
    }
}
