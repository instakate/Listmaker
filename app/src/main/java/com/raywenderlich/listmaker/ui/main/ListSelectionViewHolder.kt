package com.raywenderlich.listmaker.ui.main

import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.listmaker.databinding.ListSelectionViewHolderBinding

// 7- Allows you to pass in the ViewBinding for the ViewHolder and have it extend RecyclerView.ViewHolder
class ListSelectionViewHolder (val binding: ListSelectionViewHolderBinding) : RecyclerView.ViewHolder(binding.root) {
}