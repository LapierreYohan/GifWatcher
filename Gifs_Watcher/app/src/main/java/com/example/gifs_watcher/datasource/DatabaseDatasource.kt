package com.example.gifs_watcher.datasource

import com.example.gifs_watcher.database.AppDatabase
import com.example.gifs_watcher.models.User

object DatabaseDatasource {

    suspend fun insertUser(user: User) {
        AppDatabase.getInstance().userDao().insertUser(user)
    }

    suspend fun deleteUser(user: User) {
        AppDatabase.getInstance().userDao().deleteUser(user)
    }

    suspend fun getAllUsers(): List<User> {
        return AppDatabase.getInstance().userDao().getAllUsers()
    }

    suspend fun getUserByMail(mail: String): User {
        return AppDatabase.getInstance().userDao().getUserByMail(mail)
    }

    suspend fun getUserByUsername(username: String): User {
        return AppDatabase.getInstance().userDao().getUserByUsername(username)
    }

    suspend fun isEmailUsed(mail: String): Boolean {
        return AppDatabase.getInstance().userDao().isEmailUsed(mail)
    }

    suspend fun isUsernameUsed(username: String): Boolean {
        return AppDatabase.getInstance().userDao().isUsernameUsed(username)
    }
}