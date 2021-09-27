package com.raywenderlich.listmaker.ui.detail.ui.detail

import androidx.lifecycle.ViewModel
import com.raywenderlich.listmaker.models.TaskList

// 10 - Inform the Fragment when a new task is available
// Add a TaskList property into ListDetailViewModel
// Add a method into the ViewModel to add tasks to the lists (invokes the lambda added)
class ListDetailViewModel() : ViewModel() {
    lateinit var onTaskAdded: (() -> Unit)
    lateinit var list: TaskList

    fun addTask(task: String) {
        list.tasks.add(task)
        onTaskAdded.invoke()
    }
}