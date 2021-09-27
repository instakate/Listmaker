package com.raywenderlich.listmaker.models

import android.os.Parcel
import android.os.Parcelable

// 8 - Want to pass any TaskList through an Intent

// 8 - Primary constructor - can be given a name and list of associated tasks
// Implements the Parcelable interface which breaks down object into type the Intent system can use
class TaskList(val name: String, val tasks: ArrayList<String> = ArrayList()) : Parcelable {

    // 8-1 - Add a second constructor so a TaskList object can be created from a passed-in Parcel
    constructor(source: Parcel) : this(
        // Gets values from Parcel for the title and list of tasks, passes to primary constructor using this
        // !! makes return optionals non-optional
        source.readString()!!,
        source.createStringArrayList()!!
    )

    override fun describeContents() = 0

    // 8-2 - Called when a Parcel needs to be created from the TaskList object
    // The parcel being created is handled into this function. Fill Parcel in with write functions
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeStringList(tasks)
    }

    // 8-3 - Fulfill static interface requirements
    // Parcelable protocol requires a public static Parcelable.Creator<T> CREATOR field
    // static methods don't exist in Kotlin so create a companion object
    companion object CREATOR : Parcelable.Creator<TaskList> {

        // 8-4 - Override the interface function createFromParcel
        // Pass the parcel you get from this function to second constructor
        // Gives new TaskList with all of the data from the Parcel
        override fun createFromParcel(source: Parcel): TaskList = TaskList(source)
        override fun newArray(size: Int): Array<TaskList?> = arrayOfNulls(size)
    }
}