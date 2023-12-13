package com.example.gifs_watcher.views.splashscreen

import android.os.Build
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.repositories.UserRepository
import com.example.gifs_watcher.utils.enums.UserErrors
import com.example.gifs_watcher.models.responses.UserResponse
import kotlinx.coroutines.launch
import java.time.LocalDate

class SplashScreenViewModel() : ViewModel() {

    private var userRepo : UserRepository = UserRepository

    private val _loggedLiveData: MutableLiveData<UserResponse> = MutableLiveData()
    val loggedLiveData : LiveData<UserResponse> = _loggedLiveData

    private val _signinLiveData: MutableLiveData<UserResponse> = MutableLiveData()
    val signinLiveData : LiveData<UserResponse> = _signinLiveData

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    fun login(id : String, password : String) : Unit {

        var response = UserResponse()

        if (id.isBlank()) {
            response.addError(UserErrors.ID_EMPTY)
        } else if (password.isBlank()) {
            response.addError(UserErrors.PASSWORD_IS_EMPTY)
        }

        if (response.failed()) {
            _loggedLiveData.postValue(response)
            return
        }

        viewModelScope.launch {
            userRepo.verifyConnectionData(id.lowercase(), password)
                .collect {
                    _loggedLiveData.postValue(it)
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun register(username : String, password : String, confirmPassword : String, mail : String, birthdate : String) : Unit {

        var response = UserResponse()

        if (username.isBlank()) {
            response.addError(UserErrors.USERNAME_IS_EMPTY)
        }
        if (username.length < 3) {
            response.addError(UserErrors.USERNAME_TOO_SHORT)
        } else if (username.length > 20) {
            response.addError(UserErrors.USERNAME_TOO_LONG)
        }

        if (password.isBlank()) {
            response.addError(UserErrors.PASSWORD_IS_EMPTY)
        }
        if (mail.isBlank()) {
            response.addError(UserErrors.EMAIL_IS_EMPTY)
        }
        if (birthdate.isBlank()) {
            response.addError(UserErrors.BIRTHDATE_IS_EMPTY)
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            response.addError(UserErrors.EMAIL_NOT_VALID)
        }
        if (!isPasswordValid(password)) {
            response.addError(UserErrors.PASSWORD_TOO_SHORT)
        }
        if (password != confirmPassword) {
            response.addError(UserErrors.PASSWORDS_NOT_MATCHING)
        }
        if (birthdate.length != 10) {
            response.addError(UserErrors.BIRTHDATE_NOT_VALID)
        }

        try {
            val localDate = LocalDate.parse(birthdate)
            val current = LocalDate.now()
            val age = current.year - localDate.year

            if (age < 15) {
                response.addError(UserErrors.BIRTHDATE_TOO_YOUNG)
            }
        } catch (e : Exception) {
            response.addError(UserErrors.BIRTHDATE_NOT_VALID)
        }

        var usernameRegex = Regex("^[a-zA-ZÀ-ÿ0-9_]{3,}\$")
        if (!username.matches(usernameRegex)) {
            response.addError(UserErrors.USERNAME_CONTAINS_EXCEPTED_CHARACTERS)
        }

        var emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
        if (!mail.matches(emailRegex)) {
            response.addError(UserErrors.EMAIL_CONTAINS_EXCEPTED_CHARACTERS)
        }

        if (response.failed()) {
            _signinLiveData.postValue(response)
            return
        }

        val user = User(
            username = username.lowercase(),
            displayname = username,
            mail = mail.lowercase(),
            password = password,
            birthdate = birthdate,
        )

        viewModelScope.launch {

            userRepo.isUsernameUsed(username.lowercase())
                .collect {
                    if (it) {
                        response.addError(UserErrors.USERNAME_ALREADY_USED)
                    }
                }

            userRepo.isMailUsed(mail.lowercase())
                .collect {
                    if (it) {
                        response.addError(UserErrors.EMAIL_ALREADY_USED)
                    }
                }

            if (response.failed()) {
                _signinLiveData.postValue(response)
                return@launch
            }

            userRepo.registerUser(user)
                .collect {
                    _signinLiveData.postValue(it)
                }
        }
    }
}