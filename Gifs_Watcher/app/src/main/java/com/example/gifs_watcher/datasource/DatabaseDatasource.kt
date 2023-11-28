package com.example.gifs_watcher.datasource

import com.example.gifs_watcher.database.LocalDatabase
import com.example.gifs_watcher.models.User

object DatabaseDatasource {

    suspend fun insertUser(user: User) {
        LocalDatabase.getInstance().userDao().insertUser(user)
    }

    suspend fun deleteUser(user: User) {
        LocalDatabase.getInstance().userDao().deleteUser(user)
    }

    suspend fun getAllUsers(): List<User> {
        return LocalDatabase.getInstance().userDao().getAllUsers()
    }

    suspend fun getUserByMail(mail: String): User {
        return LocalDatabase.getInstance().userDao().getUserByMail(mail)
    }

    suspend fun getUserByUsername(username: String): User {
        return LocalDatabase.getInstance().userDao().getUserByUsername(username)
    }

    suspend fun isEmailUsed(mail: String): Boolean {
        return LocalDatabase.getInstance().userDao().isEmailUsed(mail)
    }

    suspend fun isUsernameUsed(username: String): Boolean {
        return LocalDatabase.getInstance().userDao().isUsernameUsed(username)
    }
}