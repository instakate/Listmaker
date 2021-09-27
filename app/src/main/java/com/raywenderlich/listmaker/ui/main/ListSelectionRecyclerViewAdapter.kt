package com.raywenderlich.listmaker.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.listmaker.databinding.ListSelectionViewHolderBinding
import com.raywenderlich.listmaker.models.TaskList

// Extend class to inherit from RecyclerView.Adapter<ListSelectionViewHolder>()
// Pass in the type of ViewHolder you want the RecyclerView adapter to use
// 8 - Class definition updated to add lists - a MutableList of TaskList - in primary constructor
// 9 - Pass in a clickListener ListSelectionRecyclerViewClickListener for taps on list
class ListSelectionRecyclerViewAdapter(val lists : MutableList<TaskList>,
                                       val clickListener: ListSelectionRecyclerViewClickListener) : RecyclerView.Adapter<ListSelectionViewHolder>(){

    // 7 - Array of strings to use as the list titles
    // -- no longer needed - use lists instead (from constructor)
    // val listTitles = arrayOf("Shopping List", "Chores", "Android Tutorials")

    // 9 - Create an interface on the RecyclerView to communicate with the Activity whenever a list item is tapped
    // The ViewHolder can inform the RecyclerView of any taps
    interface ListSelectionRecyclerViewClickListener {
        fun listItemClicked(list: TaskList)
    }

    // 7 - Updated to use the view binding generated for the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSelectionViewHolder {

        // 7-1 - Creates a LayoutInflater object from the parent context and uses the binding class to inflate itself
        // Creates a new binding class that allows you to bind data to the view
        val binding = ListSelectionViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        // 7-2 - A ListSelectionViewHolder object is created, passing in the binding. The ViewHolder is returned from the method.
        return ListSelectionViewHolder(binding)
    }

    // 7 - Set a value for each of the TextViews on the ViewHolder
    // For each call of onBindViewHolder, populate TextViews created in ViewHolder
    // with position in list and name of list

    override fun onBindViewHolder(holder: ListSelectionViewHolder, position: Int) {
        holder.binding.itemNumber.text = (position + 1).toString()
        holder.binding.itemString.text = lists[position].name

        // 9 - Add an onClickListener to the View of itemHolder
        holder.itemView.setOnClickListener {
            clickListener.listItemClicked(lists[position])
        }
    }

    // 7 - Determines how many items the RecyclerView has, matches the size of the RecyclerView
    override fun getItemCount(): Int {
        return lists.size
    }

    // 8 - Let the adapter know there is a new list to display
    fun listsUpdated() {

        // 8 - Inform the adapter that you updated the data source, which updates the RecyclerView
        // The data source is the ArrayList passed into the ListSelectionRecyclerViewAdapter
        notifyItemInserted(lists.size-1)
    }
}