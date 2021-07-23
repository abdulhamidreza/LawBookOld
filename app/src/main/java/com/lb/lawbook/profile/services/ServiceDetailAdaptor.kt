package com.lb.lawbook.profile.services

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lb.lawbook.R
import com.lb.lawbook.pojos.Service
import kotlinx.android.synthetic.main.services_list_item.view.*


class ServiceDetailAdaptor(val userList: ArrayList<Service>) : RecyclerView.Adapter<ServiceDetailAdaptor.ViewHolder>() {


    lateinit var customListner: CustomButtonListener

    interface CustomButtonListener {
        fun onButtonClickedListener(position: Int)
    }

    fun setCustomButtonListner(listener: CustomButtonListener) {
        this.customListner = listener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
                LayoutInflater.from(parent.context).inflate(R.layout.services_list_item, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(userList[position])
        holder.itemView.edit_service_imageView.setOnClickListener({
            customListner.onButtonClickedListener(position)
        })

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: Service) {
            for (lang in user.lang) {
                itemView.item_service_languages_value.text =
                        lang + " | " + itemView.item_service_languages_value.text
            }
            itemView.item_service_locations_value.text = user.locations
            itemView.item_service_name_value.text = user.service_type
            itemView.item_service_yxp_value.text = user.exp

            var modeString: String = ""
            var call = user.modes.get("call")?.toString()
            if (call != null) {
                modeString = "Call : " + call
            }
            var visit = user.modes.get("visit")?.toString()
            if (visit != null) {
                modeString = modeString + " | Visit : " + visit
            }
            var call_visit = user.modes.get("call_visit")?.toString()
            if (call_visit != null) {
                modeString = modeString + " | Call-Visit : " + call_visit
            }
            itemView.item_service_modes_value.text = modeString

        }
    }
}
