package com.raywenderlich.listmaker.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.raywenderlich.listmaker.models.TaskList

// 8-1 Set up ViewModel - update constructor to store a SharedPreferences property
// allows you to write key-value pairs to SharedPreferences
// 8-2 Add onListAdded lambda, used to inform other classes when list is added
// 8-3 Lazy lists: until you create the property, the property is empty - prevents unnecessary data querying
// When called, populated by retrieveLists
// 11 - TaskList and addTask used by ListDetailFragment
// 8-4 retrieveLists() method gets all the saved TaskLists from SharedPreferences
// Loops through them and recreates TaskList objects from HashSets
// 8-5 TaskList parameter is saved to sharedPreferences as a set of strings

class MainViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {

    lateinit var onListAdded: (() -> Unit)
    val lists: MutableList<TaskList> by lazy {
        retrieveLists()
    }

    lateinit var list: TaskList
    lateinit var onTaskAdded: (() -> Unit)

    fun addTask(task: String) {
        list.tasks.add(task)
        onTaskAdded.invoke()
    }

    private fun retrieveLists(): MutableList<TaskList> {
        val sharedPreferencesContents = sharedPreferences.all
        val taskLists = ArrayList<TaskList>()

        for (taskList in sharedPreferencesContents) {
            val itemsHashSet = ArrayList(taskList.value as HashSet<String>)
            val list = TaskList(taskList.key, itemsHashSet)
            taskLists.add(list)
        }
        return taskLists
    }

    // Update lists
    // Invoke lambda to let interested classes know about the new list
    fun saveList(list: TaskList) {
        sharedPreferences.edit().putStringSet(list.name,
            list.tasks.toHashSet()).apply()
        lists.add(list)
        onListAdded.invoke()
    }

    // 10 - Write the passed-in list to SharedPreferences.
    // Any existing lists with the same name are overwritten.
    fun updateList(list: TaskList) {
        sharedPreferences.edit().putStringSet(list.name,
            list.tasks.toHashSet()).apply()
        lists.add(list)
    }

    // 10 - Clears the list property of all values then adds the values from SharedPreferences
    fun refreshLists() {
        lists.clear()
        lists.addAll(retrieveLists())
    }
}