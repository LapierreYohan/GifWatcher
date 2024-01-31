package com.example.gifs_watcher.views.splashscreen

import android.content.Context
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
import com.example.gifs_watcher.models.responses.Response
import com.example.gifs_watcher.repositories.GifRepository
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

class SplashScreenViewModel : ViewModel() {

    private var userRepo : UserRepository = UserRepository
    private var gifRepo : GifRepository = GifRepository

    private val _loggedLiveData: MutableLiveData<Response<User>> = MutableLiveData()
    val loggedLiveData : LiveData<Response<User>> = _loggedLiveData

    private val _signinLiveData: MutableLiveData<Response<User>> = MutableLiveData()
    val signinLiveData : LiveData<Response<User>> = _signinLiveData

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    fun login(id : String, password : String) {

        val response = Response<User>()

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
    fun register(username : String, password : String, confirmPassword : String, mail : String, birthdate : String) {

        val response = Response<User>()
        val convertedBirthdate = convertDateFormat(birthdate)

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
        if (convertedBirthdate.isBlank()) {
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
        if (convertedBirthdate.length != 10) {
            response.addError(UserErrors.BIRTHDATE_NOT_VALID)
        }

        try {
            val localDate = LocalDate.parse(convertedBirthdate)
            val current = LocalDate.now()
            val age = current.year - localDate.year

            if (age < 15) {
                response.addError(UserErrors.BIRTHDATE_TOO_YOUNG)
            }
        } catch (e : Exception) {
            response.addError(UserErrors.BIRTHDATE_NOT_VALID)
        }

        val usernameRegex = Regex("^[a-zA-ZÀ-ÿ0-9_]{3,}\$")
        if (!username.matches(usernameRegex)) {
            response.addError(UserErrors.USERNAME_CONTAINS_EXCEPTED_CHARACTERS)
        }

        val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
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
            birthdate = convertedBirthdate,
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

    private fun convertDateFormat(inputDate: String): String {
        val inputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        try {
            val date = inputDateFormat.parse(inputDate)
            return outputDateFormat.format(date!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return inputDate
    }

    fun prepareGifs(context: Context) {
        viewModelScope.launch {
            gifRepo.prepareGifs(context)
        }
    }
}