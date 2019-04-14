package com.example.kanwal_laptop.lecture10_kotlin

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import java.sql.SQLException

class myDBHandler(private val mContext: Context) : SQLiteOpenHelper
    (mContext, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        try {
            db!!.execSQL(CREATE_TABLE)
            Toast.makeText(mContext, "Table created", Toast.LENGTH_SHORT).show()
        } catch (ex: SQLException) {
            Log.i("exception", ex.toString())
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        try {
            db!!.execSQL(DROP_TABLE)
            Toast.makeText(mContext, "Table upgraded", Toast.LENGTH_SHORT).show()
        } catch (ex: SQLException) {
            Log.i("exception", ex.toString())
        }
    }

    fun insertData(userName: String, userGender: String): Long {
        val sqLiteDatabase = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USER_NAME, userName)
        values.put(COLUMN_USER_GENDER, userGender)
        return sqLiteDatabase.insert(TABLE_NAME, null, values)
    }

    fun printDatabase(): String {
        val sqLiteDatabase = writableDatabase
        val columns = arrayOf(COLUMN_ID, COLUMN_USER_NAME, COLUMN_USER_GENDER)
        val cursor = sqLiteDatabase.query(TABLE_NAME, columns, null, null, null, null, null)

        val buffer = StringBuffer()
        while (cursor.moveToNext()) {
            val id_index = cursor.getColumnIndex(COLUMN_ID)
            val id_value = cursor.getInt(id_index)

            val user_index = cursor.getColumnIndex(COLUMN_USER_NAME)
            val user_value = cursor.getString(user_index)

            val gender_index = cursor.getColumnIndex(COLUMN_USER_GENDER)
            val gender_value = cursor.getString(gender_index)

            buffer.append("$id_value : $user_value : $gender_value")
        }
        return buffer.toString()
    }

    fun searchData(name: String): String {
        val sqLiteDatabase = writableDatabase
        val columns = arrayOf(COLUMN_ID, COLUMN_USER_NAME, COLUMN_USER_GENDER)
        val cursor = sqLiteDatabase.query(TABLE_NAME, columns, "$COLUMN_USER_NAME = '$name'", null, null, null, null)

        val buffer = StringBuffer()
        while (cursor.moveToNext()) {
            val id_index = cursor.getColumnIndex(COLUMN_ID)
            val id_value = cursor.getInt(id_index)

            val user_index = cursor.getColumnIndex(COLUMN_USER_NAME)
            val user_value = cursor.getString(user_index)

            val gender_index = cursor.getColumnIndex(COLUMN_USER_GENDER)
            val gender_value = cursor.getString(gender_index)

            buffer.append("$id_value : $user_value : $gender_value")
        }
        return buffer.toString()
    }

    fun deleteData(userToDelete: String): Int {
        val sqLiteDatabase = writableDatabase
        val whereArgs = arrayOf(userToDelete)
        return sqLiteDatabase.delete(TABLE_NAME, "$COLUMN_USER_NAME =?", whereArgs)
    }

    fun updateData(oldName: String, newName: String, gender: String): Int {
        val sqLiteDatabase = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USER_NAME, newName)
        values.put(COLUMN_USER_GENDER, gender)
        val whereArgs = arrayOf(oldName)
        return sqLiteDatabase.update(TABLE_NAME, values, "$COLUMN_USER_NAME=?", whereArgs)

    }

    companion object {
        private val DATABASE_NAME = "students_record.db"
        private val DATABASE_VERSION = 1

        private val TABLE_NAME = "User_Info"
        private val COLUMN_ID = "_id"
        private val COLUMN_USER_NAME = "user_name"
        private val COLUMN_USER_GENDER = "user_gender"


        private val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "$COLUMN_USER_NAME VARCHAR(255) ," +
                "$COLUMN_USER_GENDER VARCHAR(16))"
        //        private val CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
//                                    COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT ," +
//                                    COLUMN_USER_NAME + "VARCHAR(255) ," +
//                                    COLUMN_USER_GENDER + "VARCHAR(16);"
        private val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

}