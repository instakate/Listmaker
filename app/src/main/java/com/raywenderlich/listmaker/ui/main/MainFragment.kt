package com.raywenderlich.listmaker.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.listmaker.databinding.MainFragmentBinding
import com.raywenderlich.listmaker.models.TaskList

// 6 - Fragment used in MainActivity.kt, where views will be placed
// 9 - Interface to pass list from the Fragment to the Activity
// 11 - Move the clickListener into the class so it exists as a property.
// 6 - Var to hold binding - lateinit tells compiler var will be created in future

class MainFragment : Fragment(), ListSelectionRecyclerViewAdapter.ListSelectionRecyclerViewClickListener {

    lateinit var clickListener: MainFragmentInteractionListener
    private lateinit var binding: MainFragmentBinding

    interface MainFragmentInteractionListener {
        fun listItemTapped(list: TaskList)
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    // 6 - link to the binding which contains properties for each view
    // Use binding to access listsRecyclerView and use linear layout format
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = MainFragmentBinding.inflate(inflater, container,false)
        binding.listsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
         return binding.root
    }

    // 8 - Use the ViewModel
    // 8 - adapter creation - Create an adapter once the ViewModel is available
    // 9 - this - Pass in ListSelectionRecyclerViewClickListener() (MainFragment) to the adapter
    // Assign the adapter to the RecyclerView using the ViewBinding
    // Use the onListAdded lambda to listen for added lists
    // When a list is added, recyclerViewAdapter.listsUpdated is called

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(),
            MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(requireActivity()))).get(MainViewModel::class.java)

        val recyclerViewAdapter = ListSelectionRecyclerViewAdapter(viewModel.lists, this)

        binding.listsRecyclerView.adapter = recyclerViewAdapter

        viewModel.onListAdded = {
            recyclerViewAdapter.listsUpdated()
        }
    }

    // 9 - Implement the method to conform to the interface
    // Whenever a tap happens on a list item in the recyclerView,
    // the Fragment is informed about it and calls clickListener.listItemTapped(),
    // passing in the list the user taps on.
    // clickListener passes the list from the Fragment to the Activity, where we can pass the list
    // into the second Activity
    override fun listItemClicked(list: TaskList) {
        clickListener.listItemTapped(list)
    }
}