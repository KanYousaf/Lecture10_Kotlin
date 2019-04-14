package com.example.kanwal_laptop.lecture10_kotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sqldatabase.*

class SQLDatabaseActivity : AppCompatActivity() {
    private var mDBHandler: myDBHandler? = null
    private var userNameEntered: String? = null
    private var userGenderSelected: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sqldatabase)
        mDBHandler = myDBHandler(this)
        printDatabase()
    }

    fun insertBtnClicked(view: View) {
        inputData()
        if (userNameEntered.equals("")) {
            Toast.makeText(this, "Enter User Name", Toast.LENGTH_SHORT).show()
        } else {
            val id = mDBHandler?.insertData(userNameEntered!!, userGenderSelected!!)
            if (id!! > 0) {
                toastSuccessMessage()
            } else {
                toastFailureMessage()
            }
        }
        printDatabase()
    }

    fun deleteBtnClicked(view: View) {
        inputData()
        if (userNameEntered.equals("")) {
            Toast.makeText(this, "Enter User Name", Toast.LENGTH_SHORT).show()
        } else {
            val count = mDBHandler?.deleteData(userNameEntered!!)
            if (count!! > 0) {
                toastSuccessMessage()
            } else {
                toastFailureMessage()
            }
        }
        printDatabase()
    }

    fun searchBtnClicked(view: View) {
        inputData()
        if (userNameEntered.equals("")){
            Toast.makeText(this, "Enter User Name", Toast.LENGTH_SHORT).show()
        }  else {
            display_results!!.setText(mDBHandler!!.searchData(userNameEntered!!))
        }
    }

    fun updateBtnClicked(view: View) {
        inputData()
        if (userNameEntered.equals("") || newNameET.text.toString().equals("")) {
            Toast.makeText(this, "Enter User Old and New Name", Toast.LENGTH_SHORT).show()
        } else {
            val count = mDBHandler?.updateData(userNameEntered!!, newNameET.text.toString(), userGenderSelected!!)
            if (count!! > 0) {
                toastSuccessMessage()
            } else {
                toastFailureMessage()
            }
        }
        printDatabase()


    }

    fun printDatabase() {
        display_results.text = mDBHandler!!.printDatabase()
        userNameET.setText("")
        gender_switch.isChecked = false
    }

    fun inputData() {
        userNameEntered = userNameET.text.toString()
        userGenderSelected = if (gender_switch.isChecked) "M" else "F"
    }

    fun toastSuccessMessage() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
    }

    fun toastFailureMessage() {
        Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
    }
}
