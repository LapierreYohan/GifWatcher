package com.example.gifs_watcher.viewmodel

import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gifs_watcher.repositories.UserRepository
import com.example.gifswatcher.models.User
import kotlinx.coroutines.launch

class SplashScreenViewModel() : ViewModel() {

    private var userRepo : UserRepository = UserRepository

    private val _userLiveData: MutableLiveData<User?> = MutableLiveData()
    val userLiveData : LiveData<User?> = _userLiveData

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

    private fun login(id : String, password : String) : Unit {
        viewModelScope.launch {
            userRepo.verifyConnectionData(id, password)
                .collect {
                    it?.let { userLogged ->
                        _userLiveData.postValue(userLogged)
                    }
                }
        }
    }
}