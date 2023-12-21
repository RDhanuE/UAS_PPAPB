package com.example.calorify20.mainApp

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

class PrefManager private constructor(context: Context){

    private val sharedPreferences : SharedPreferences

    companion object {
        private const val PREFS_FILENAME = "AuthAppPrefs"
        private const val IS_LOGGED_IN = "isLoggedIn"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
        private const val DATE = "time"
        private lateinit var editor: Editor

        @Volatile
        private var instance : PrefManager? = null

        fun getInstance(context: Context) : PrefManager {
            return instance ?: synchronized(this){
                instance ?: PrefManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }
    init {
        sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun setLoggedIn(isLoggedIn : Boolean) {
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun isLoggedIn() : Boolean {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }

    fun saveEmail(email : String) {
        editor.putString(EMAIL, email)
        editor.apply()
    }

    fun savePassword(password : String){
        editor.putString(PASSWORD, password)
    }

    fun getEmail() : String {
        return sharedPreferences.getString(EMAIL, "") ?: ""
    }

    fun getPassword() : String {
        return sharedPreferences.getString(PASSWORD, "") ?: ""
    }

    fun clear() {
        editor.remove(EMAIL)
        editor.remove(PASSWORD)
        editor.remove(IS_LOGGED_IN)
        editor.apply()
    }

    fun getSavedDate(): String {
        return sharedPreferences.getString(DATE, "") ?: ""
    }

    fun saveCurrentDate(currentDate: Date) {
        val editor = sharedPreferences.edit()
        editor.putString(DATE, SimpleDateFormat("yyyy-MM-dd").format(currentDate))
        editor.apply()
    }
}