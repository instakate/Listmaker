package com.raywenderlich.listmaker.ui.detail.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.listmaker.MainActivity
import com.raywenderlich.listmaker.R
import com.raywenderlich.listmaker.databinding.ListDetailFragmentBinding
import com.raywenderlich.listmaker.models.TaskList
import com.raywenderlich.listmaker.ui.main.MainViewModel
import com.raywenderlich.listmaker.ui.main.MainViewModelFactory

class ListDetailFragment : Fragment() {
    companion object {
        fun newInstance() = ListDetailFragment()
    }

    // 10 - Hold the reference to the ViewBinding for the Fragment
    // 10-1 Use the binding class to acquire the layout for the Fragment
    // 10-2 Return the root of the View for your Fragment

    private lateinit var viewModel: MainViewModel
    lateinit var binding: ListDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    // 9 - Use requireActivity() - ViewModel is shared between the activity and Fragment
    // ensures same instance of MainViewModel is returned
    // 11 - Get the list by grabbing the bundle passed in from MainActivity
    // 10 - Set up the recyclerView and notify ListDetailFragment about the added task
    // Create a RecyclerAdapter and LinearLayoutManager assigned to listItemsRecyclerView through
    // the view binding, and assign a callback to the lambda you added to the ViewModel
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(),
            MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(requireActivity()))
        ).get(MainViewModel::class.java)

        val list: TaskList? =
            arguments?.getParcelable(MainActivity.INTENT_LIST_KEY)
        if (list != null) {
            viewModel.list = list
            requireActivity().title = list.name
        }
        val recyclerAdapter =  ListItemsRecyclerViewAdapter(viewModel.list)
        binding.listItemsRecyclerview.adapter = recyclerAdapter
        binding.listItemsRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        viewModel.onTaskAdded = {
            recyclerAdapter.notifyDataSetChanged()
        }
    }
}
