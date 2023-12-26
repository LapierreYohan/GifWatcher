package com.example.gifs_watcher.repositories

import com.example.gifs_watcher.cache.CacheDatasource
import com.example.gifs_watcher.database.DistantDatabaseDatasource
import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.models.responses.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object UserRepository {
    private var cache : CacheDatasource = CacheDatasource
    private  var database : DistantDatabaseDatasource = DistantDatabaseDatasource

    fun verifyConnectionData(id : String, password : String) : Flow<Response<User>> = flow {

        database.login(id, password).collect {
            if (it.success()) {
                cache.setAuthUser(it.data())
            }
            emit(it)
        }
    }

    fun getAuthUser() : User? {
        return cache.getAuthUser()
    }

    fun registerUser(userToInsert : User) : Flow<Response<User>> = flow {

         database.register(userToInsert).collect {
             cache.setAuthUser(it.data())
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