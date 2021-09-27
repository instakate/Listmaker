package com.raywenderlich.listmaker.ui.detail

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.raywenderlich.listmaker.MainActivity
import com.raywenderlich.listmaker.R
import com.raywenderlich.listmaker.databinding.ListDetailActivityBinding
import com.raywenderlich.listmaker.ui.detail.ui.detail.ListDetailFragment
import com.raywenderlich.listmaker.ui.detail.ui.detail.ListDetailViewModel
import com.raywenderlich.listmaker.ui.main.MainViewModel
import com.raywenderlich.listmaker.ui.main.MainViewModelFactory

class ListDetailActivity : AppCompatActivity() {

    // 10 - Hold the reference for the ViewBinding, ViewModel and ListDetailFragment
    // 10 - Inflate the layout using the generated binding, assign it to the binding property,
    // set the content view with the root View, and add a click listener to the button
    // 10 - Acquire the ViewModel
    // 9-1 - Pass in a list from MainActivity.kt via an intent and
    // 9-2 - set the title of the Activity to the name of the list

    lateinit var binding: ListDetailActivityBinding
    lateinit var viewModel: MainViewModel
    lateinit var fragment: ListDetailFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ListDetailActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.addTaskButton.setOnClickListener {
            showCreateTaskDialog()
        }

        viewModel = ViewModelProvider(this, MainViewModelFactory(
            PreferenceManager.getDefaultSharedPreferences(this))).get(MainViewModel::class.java)
        viewModel.list = intent.getParcelableExtra(MainActivity.INTENT_LIST_KEY)!!
        title = viewModel.list.name

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.detail_container, ListDetailFragment.newInstance())
                .commitNow()
        }
    }

    // 10 - Show a dialog to the user, asking for the task to add to the list
    // 10-1 Create an EditText so you can receive text input from the user
    // 10-2 Create an AlertDialogBuilder and use method chaining to set up aspects of the AlertDialog.
    // When any method is called on the Builder, it returns the builder instance, modified.
    // 10-3 Access the EditText to grab the text input and create a task from the input
    // 10-4 Notify the ViewModel a new item was added. ViewModel has a chance to update
    // the list and inform the Fragment to update the RecyclerView Adapter.
    // 10-5 Close the dialog by dismissing it.
    // 10-6 - Method chaining creates and shows the AlertDialog without needing to have
    // the AlertDialogBuilder as a separate variable.

    private fun showCreateTaskDialog() {
        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(this)
            .setTitle(R.string.task_to_add)
            .setView(taskEditText)
            .setPositiveButton(R.string.add_task) { dialog, _ ->
                val task = taskEditText.text.toString()
                viewModel.addTask(task)
                dialog.dismiss()
            }
            .create()
            .show()
    }

    // 10 - Run code whenever back button is tapped to get back to MainActivity
    // Bundle up the list and put it into an Intent
    // Set the result to RESULT_OK and pass in the Intent

    override fun onBackPressed() {
        val bundle = Bundle()
        bundle.putParcelable(MainActivity.INTENT_LIST_KEY, viewModel.list)
        val intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }

}