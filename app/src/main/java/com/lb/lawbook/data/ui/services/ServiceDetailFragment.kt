package com.lb.lawbook.data.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.lb.lawbook.R
import kotlinx.android.synthetic.main.fragment_service_detail.*


class ServiceDetailFragment : Fragment() {

    private lateinit var arrayChecked: BooleanArray


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Initialize a boolean array of checked items
        arrayChecked = booleanArrayOf(false, false, false, false, false, false)
        languages_TextView.setOnClickListener { showDialog() }

    }

    // Method to show an alert dialog with multiple choice list items
    private fun showDialog() {
        // Late initialize an alert dialog object
        lateinit var dialog: AlertDialog
        // Initialize an array of Lang
        val arrayLang = arrayOf("HINDI", "ENGLISH", "MARATHI", "KANADA", "TAMIL")
        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(requireContext())

        // Set a title for alert dialog
        builder.setTitle("Select Languages")

        // Define multiple choice items for alert dialog
        builder.setMultiChoiceItems(arrayLang, arrayChecked, { dialog, which, isChecked ->
            // Update the clicked item checked status
            arrayChecked[which] = isChecked

        })

        // Set the positive/yes button click listener
        builder.setPositiveButton("OK") { _, _ ->
            // Do something when click positive button
            lang_list_textView.text = ""
            for (i in 0 until arrayLang.size) {
                val checked = arrayChecked[i]
                if (checked) {
                    lang_list_textView.text = "${lang_list_textView.text}  ${arrayLang[i]} \n"
                }
            }
        }


        // Initialize the AlertDialog using builder object
        dialog = builder.create()

        // Finally, display the alert dialog
        dialog.show()
    }
}