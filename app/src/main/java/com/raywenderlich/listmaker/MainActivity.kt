package com.raywenderlich.listmaker

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.raywenderlich.listmaker.databinding.MainActivityBinding
import com.raywenderlich.listmaker.models.TaskList
import com.raywenderlich.listmaker.ui.detail.ListDetailActivity
import com.raywenderlich.listmaker.ui.detail.ui.detail.ListDetailFragment
import com.raywenderlich.listmaker.ui.main.MainFragment
import com.raywenderlich.listmaker.ui.main.MainViewModel
import com.raywenderlich.listmaker.ui.main.MainViewModelFactory

// 6 - Code related to the layout and fragment in the app
// 9 - Updated class definition to implement the MainFragmentInteractionListener interface
class MainActivity : AppCompatActivity(), MainFragment.MainFragmentInteractionListener {

    // 8 Create properties to store the binding and ViewModel
    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* 8 Create the ViewModel
        / ViewModelProvider is a store holding a reference to ViewModels for a particular Scope
        / (lifetime of a ViewModel)
        / 'this' attaches it to MainActivity
        / ViewModels don't expect to have properties in their constructors, so pass in ViewModelFactory
        / ViewModelFactory accepts an instance of SharedPreferences, used to save and retrieve lists
        / MainViewModel::class.java specifies what type of ViewModel should be retrieved from the provider */
        /* 11 - Update savedInstanceState to check the right View to contain MainFragment
        / Create a new instance of MainFragment() and set the clickListener to be the Activity
        / Create a variable to hold the id of the FragmentContainerView
        / The ViewBinding is checked to see if mainFragmentContainer is null to make sure the Activity uses the right one
        / Set up the view to show the Fragment using the supportFragmentManager (handles presenting and removing Fragments)
        / When commit is called, the MainFragment is added to the container view identified by the view ID
        // 8 Set up the binding and the onClickListener */

        viewModel = ViewModelProvider(this, MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(this))).get(MainViewModel::class.java)
        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (savedInstanceState == null) {
            val mainFragment = MainFragment.newInstance()
            mainFragment.clickListener = this
            val fragmentContainerViewId: Int = if (binding.mainFragmentContainer == null) {
                R.id.detail_container
            } else {
                R.id.main_fragment_container
            }
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(fragmentContainerViewId, mainFragment)
            }
        }

        binding.fabButton.setOnClickListener {
            showCreateListDialog()
        }
    }

    // 8 Method creates an AlertDialog to get the name of the list from the user
    // 8-1 Retrieve the strings defined in strings.xml for the dialog
    // 8-2 AlertDialog.Builder constructs the dialog.
    // Input field for user to enter name of list
    // TYPE_CLASS_TEXT shows text-based keyboard
    // Set title of dialog and content view of dialog
    // 8-3 Add a positive button to the dialog labeled with positiveButtonTitle
    // onClickListener, dismiss the dialog
    // 8 Create a new list in MainViewModel
    // 9 Call showListDetail - when you create a new list, passes new list into new activity
    // 8-4 Create the dialog and display on screen

    private fun showCreateListDialog() {

        val dialogTitle = getString(R.string.name_of_list)
        val positiveButtonTitle = getString(R.string.create_list)
        val builder = AlertDialog.Builder(this)
        val listTitleEditText = EditText(this)
        listTitleEditText.inputType =InputType.TYPE_CLASS_TEXT

        builder.setTitle(dialogTitle)
        builder.setView(listTitleEditText)
        builder.setPositiveButton(positiveButtonTitle) { dialog, _ -> dialog.dismiss()
            val taskList = TaskList(listTitleEditText.text.toString())
            viewModel.saveList(taskList)
            showListDetail(taskList)
        }
        builder.create().show()
    }

    // 9 - Create an Intent and pass in the current Activity and class of the Activity to show on
    // screen (currently on this screen, now want to move to that screen)
    // Extras are keys with associated values you can provide to Intents
    // - Give more info to receiver about the action to be done
    // Want to display a list and use a key to reference the list
    // Method call informs the current Activity to start another Activity with the information within the Intent
    // 11 - Check the binding to see if the FragmentContainerView from the larger layout is instantiated
    // If null, then create as usual - otherwise, a bundle is created with key and values
    // ListDetailFragment is set on other FragmentViewContainer, passing in the bundle
    // 11 The button onClickListener creates a task whenever a ListDetailFragment is shown

    private fun showListDetail(list: TaskList) {
        if (binding.mainFragmentContainer == null) {
            val listDetailIntent = Intent(this, ListDetailActivity::class.java
            )
            listDetailIntent.putExtra(INTENT_LIST_KEY, list)
            startActivityForResult(listDetailIntent, LIST_DETAIL_REQUEST_CODE )
        } else {
            val bundle = bundleOf(INTENT_LIST_KEY to list)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(
                    R.id.list_detail_fragment_container,
                    ListDetailFragment::class.java, bundle, null
                )
            }
            binding.fabButton.setOnClickListener {
                showCreateTaskDialog()
            }
        }
    }

    // 9 - Place the list in the Bundle
    // Used by the Intent to refer to a list whenever it needs to pass one to the new Activity
    // 10 - Request code to identify results from ListDetailActivity
    companion object {
        const val INTENT_LIST_KEY = "list"
        const val LIST_DETAIL_REQUEST_CODE = 123
    }

    override fun listItemTapped(list: TaskList) {
        showListDetail(list)
    }

    // 10 - Check the request code is as expected and RESULT_OK
    // Unwrap the data intent passed in (possibly null)
    // Once you confirm there is data, save the list to MainViewModel
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LIST_DETAIL_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                viewModel.updateList(data.getParcelableExtra(INTENT_LIST_KEY)!!)
                viewModel.refreshLists()
            }
        }
    }

    // Create an AlertDialog with an EditText attached where you can add your task
    // When positive button is pressed, task is taken from EditText and added to the ViewModel
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

    // 11 Use supportFragmentManager to find the ListDetailFragment shown with the view ID of the fragmentContainView
    // Check if the Fragment is null. If it is, then close the activity
    // Reset the Activity to its original state. Title of Activity changed back to ListMaker
    // Commit a transaction to remove ListDetailFragment
    // Reset the FloatingActionButton to create a list when tapped
    override fun onBackPressed() {
        val listDetailFragment =
            supportFragmentManager.findFragmentById(R.id.list_detail_fragment_container)
        if (listDetailFragment == null) {
            super.onBackPressed()
        } else {
            title = resources.getString(R.string.app_name)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                remove(listDetailFragment)
            }
            binding.fabButton.setOnClickListener {
                showCreateListDialog()
            }
        }
    }
 }
