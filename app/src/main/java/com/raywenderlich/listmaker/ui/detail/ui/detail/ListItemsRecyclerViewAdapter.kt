package com.raywenderlich.listmaker.ui.detail.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.listmaker.databinding.ListItemViewHolderBinding
import com.raywenderlich.listmaker.models.TaskList


// 10 - Class definition has a primary constructor that accepts a TaskList and conforms to RecyclerView.Adapter<ListItemViewHolder>
class ListItemsRecyclerViewAdapter(val list: TaskList) : RecyclerView.Adapter<ListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding = ListItemViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    // 10 - Hook up the data to the TextView using the ViewBinding - bind the TextView to a
    // specific task from the list depending on the position of the ViewHolder
    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.binding.textView.text = list.tasks[position]
    }

    // 10 - Tells RecyclerView how many items to display - returns the number of tasks in the list
    override fun getItemCount(): Int {
        return list.tasks.size
    }
}

