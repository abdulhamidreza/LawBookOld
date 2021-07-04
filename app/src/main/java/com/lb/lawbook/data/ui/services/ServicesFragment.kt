package com.lb.lawbook.data.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lb.lawbook.R
import kotlinx.android.synthetic.main.fragment_services_list.*


/**
 * A fragment representing a list of Items.
 */
class ServicesFragment : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_services_list, container, false)
        val viewR = view.findViewById<RecyclerView>(R.id.recycleView_services)

        // Set the adapter
        if (viewR is RecyclerView) {
            with(viewR) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyServicesItemRecyclerViewAdapter(DummyContent.ITEMS)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        add_new_service_btn.setOnClickListener { navController.navigate(R.id.action_nav_services_to_serviceDetailFragment) }

    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                ServicesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}