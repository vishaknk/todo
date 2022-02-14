package com.sample.todo.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log

/**
 * Created by Shivichu on 05-09-2017.
 */
@SuppressLint("CommitPrefEdits")
class SessionManager(var _context: Context) {
    // Shared Preferences
    var pref: SharedPreferences
    var editor: SharedPreferences.Editor

    // Shared pref mode
    private var PRIVATE_MODE = 0

    fun setLogin(isLoggedIn: Boolean) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        // commit changes
        editor.apply()
        Log.d(TAG, "User login session modified!")
    }

    fun setToken(token: String) {
        editor.putString(KEY_TOKEN, token)
        editor.apply()
    }

    val isLoggedIn: Boolean
        get() = pref.getBoolean(KEY_IS_LOGGED_IN, false)

    fun logoutUser() {
        editor.clear()
        editor.apply()
    }

    companion object {
        // LogCat tag
        private val TAG = SessionManager::class.java.simpleName

        // Shared preferences file name
        private const val PREF_NAME = "SessionManager_user_"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_TOKEN = "token"
    }

    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }
}