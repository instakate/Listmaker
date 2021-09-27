package com.raywenderlich.listmaker.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// 1 Add a constructor for the factory to receive an instance of SharedPreferences.
// Implement the ViewModelProvider.Factory interface

class MainViewModelFactory(private val sharedPreferences: SharedPreferences) : ViewModelProvider.Factory {

    // 2 override the create(modelClass: Class<T> method from the interface.
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        // Method returns an instance of MainViewModel that uses the SharedPreferences field within its constructor.
        return MainViewModel(sharedPreferences) as T
    }
}