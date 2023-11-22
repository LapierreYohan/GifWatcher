package com.example.gifs_watcher.repositories

import com.example.gifs_watcher.database.AppDatabase
import com.example.gifs_watcher.datasource.CacheDatasource
import com.example.gifs_watcher.datasource.DatabaseDatasource
import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.utils.enums.UserErrors
import com.example.gifs_watcher.utils.managers.PasswordManager
import com.example.gifs_watcher.utils.responses.UserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.security.MessageDigest

object UserRepository {
    private var cache : CacheDatasource = CacheDatasource
    private  var database : DatabaseDatasource = DatabaseDatasource

    fun verifyConnectionData(id : String, password : String) : Flow<UserResponse> = flow {

        var response = UserResponse()

        var user = database.getUserByMail(id)
        if (user == null) {
            user = database.getUserByUsername(id)
        }

        if (user != null) {
            var samePassword = PasswordManager.verifyPassword(password, user.password!!)

            if (samePassword) {
                response.addUser(user)
                cache.setAuthUser(user)
                emit(response)
            } else if (!samePassword) {
                response.addError(UserErrors.PASSWORD_INVALID)
                emit(response)
            }

        } else {
            response.addError(UserErrors.ID_NOT_FOUND)
            emit(response)
        }
    }

    fun getAuthUser() : User? {
        return cache.getAuthUser()
    }

    fun registerUser(userToInsert : User) : Flow<UserResponse> = flow {

        var response = UserResponse()
        database.insertUser(userToInsert)

        var user = database.getUserByMail(userToInsert.mail!!.lowercase())

        if (user != null) {
            response.addUser(user)
            cache.setAuthUser(user)
            emit(response)
        } else {
            response.addError(UserErrors.UNKNOWN_ERROR)
            emit(response)
        }
    }

    fun logout() {
        cache.setAuthUser(null)
    }

    fun isLogged() : Boolean {
        return cache.getAuthUser() != null
    }

    fun isEmailUsed(mail : String) : Flow<Boolean> = flow {
        emit(database.isEmailUsed(mail))
    }

    fun isUsernameUsed(username : String) : Flow<Boolean> = flow {
        emit(database.isUsernameUsed(username))
    }
}