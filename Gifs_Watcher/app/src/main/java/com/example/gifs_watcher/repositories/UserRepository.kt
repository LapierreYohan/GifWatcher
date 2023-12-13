package com.example.gifs_watcher.repositories

import com.example.gifs_watcher.datasource.CacheDatasource
import com.example.gifs_watcher.datasource.DistantDatabaseDatasource
import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.utils.responses.UserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object UserRepository {
    private var cache : CacheDatasource = CacheDatasource
    private  var database : DistantDatabaseDatasource = DistantDatabaseDatasource

    fun verifyConnectionData(id : String, password : String) : Flow<UserResponse> = flow {

        database.login(id, password).collect {
            if (it.success()) {
                cache.setAuthUser(it.user())
            }
            emit(it)
        }
    }

    fun getAuthUser() : User? {
        return cache.getAuthUser()
    }

    fun registerUser(userToInsert : User) : Flow<UserResponse> = flow {

         database.register(userToInsert).collect {
             cache.setAuthUser(it.user())
             emit(it)
         }
    }

    fun logout() {
        cache.setAuthUser(null)
    }

    fun isLogged() : Boolean {
        return cache.getAuthUser() != null
    }

    fun isUsernameUsed(username : String) : Flow<Boolean> = flow {
        database.checkUsernameAvailability(username).collect {
            emit(!it)
        }
    }

    fun isMailUsed(mail : String) : Flow<Boolean> = flow {
        database.checkMailAvailability(mail).collect {
            emit(!it)
        }
    }
}