package com.example.gifs_watcher.viewmodel

import androidx.lifecycle.ViewModel
import android.util.Patterns

class SplashScreenViewModel() : ViewModel() {

    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}