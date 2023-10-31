package com.example.gifs_watcher.viewmodel

import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.repositories.UserRepository
import kotlinx.coroutines.launch

class SplashScreenViewModel() : ViewModel() {

    private var userRepo : UserRepository = UserRepository

    private val _loggedLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val loggedLiveData : LiveData<Boolean> = _loggedLiveData

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
                        if (userLogged.idUsers != null) {
                            _loggedLiveData.postValue(true)
                        } else {
                            _loggedLiveData.postValue(false)
                        }
                    }
                }
        }
    }
}