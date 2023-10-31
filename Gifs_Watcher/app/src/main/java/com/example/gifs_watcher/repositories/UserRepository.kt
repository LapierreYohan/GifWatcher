package com.example.gifs_watcher.repositories

import com.example.gifs_watcher.datasource.CacheDataSource
import com.example.gifs_watcher.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object UserRepository {
    private var cache : CacheDataSource = CacheDataSource
    val user = User("UserTest", "1234", "User", "TEST", "test@gmail.com", "", "", "1")

    fun verifyConnectionData(id : String, password : String) : Flow<User> = flow {

       if ((id == user.username || id == user.mail) && password == user.password) {
           emit(user)
       } else {
           emit(User())
       }
    }
}