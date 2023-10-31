package com.example.gifs_watcher.viewmodel

import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.example.gifswatcher.models.User

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

    private fun login(indentifiant : String, password : String) : Unit {

    }
}