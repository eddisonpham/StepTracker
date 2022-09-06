package com.example.steptracker

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper: SQLiteOpenHelper  {
    val COLUMN_ID = "ID"
    val USER_TABLE = "USER_TABLE"
    val USER_EMAIL = "USER_EMAIL"
    val USER_PASSWORD = "USER_PASSWORD"
    val USER_STEPS = "USER_STEPS"
    constructor(context: Context)
            :super(context, "steptracker.db", null, 1){
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE $USER_TABLE"+
                "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$USER_EMAIL TEXT, " +
                "$USER_PASSWORD INT, " +
                "$USER_STEPS INT)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun createAccount(UM: UserModel): Boolean{
        val db:SQLiteDatabase = this.writableDatabase
        val cv = ContentValues()

        cv.put(USER_EMAIL, UM.email)
        cv.put(USER_PASSWORD, UM.password)
        cv.put(USER_STEPS, UM.steps)

        val insert:Long = db.insert(USER_TABLE, null, cv)
        return !insert.equals(-1)
    }

    fun updateSteps(id: Int, steps: Int): Boolean{
        val db:SQLiteDatabase = this.writableDatabase
        val cv = ContentValues()
        cv.put(USER_STEPS, steps)
        val query = "SELECT $USER_STEPS FROM $USER_TABLE WHERE $COLUMN_ID = ?"
        val arr = arrayOf(id.toString())
        val cursor = db.rawQuery(query, arr)
        val result = db.update(USER_TABLE, cv, "id=?", arr)
        return cursor.moveToFirst()
    }

    fun checkEmailExists(email: String): Cursor? {
        val db: SQLiteDatabase = this.readableDatabase
        val query = "SELECT * FROM $USER_TABLE WHERE $USER_EMAIL=?"
        val arr = arrayOf(email)
        val cursor = db.rawQuery(query, arr)
        while (cursor.moveToNext()){
            val cursorEmail = cursor.getString(1)
            if (email == cursorEmail){
                return cursor
            }
        }
        return null
    }
}