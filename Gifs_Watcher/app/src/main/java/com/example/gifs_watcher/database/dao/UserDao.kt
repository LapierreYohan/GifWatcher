package com.example.gifs_watcher.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.gifs_watcher.models.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM users WHERE LOWER(mail) = :mail")
    suspend fun getUserByMail(mail: String): User

    @Query("SELECT * FROM users WHERE LOWER(username) = :username")
    suspend fun getUserByUsername(username: String): User

    @Query("SELECT * FROM users WHERE LOWER(mail) = :mail")
    suspend fun isEmailUsed(mail: String): Boolean {
        return getUserByMail(mail) != null
    }

    @Query("SELECT * FROM users WHERE LOWER(username) = :username")
    suspend fun isUsernameUsed(username: String): Boolean {
        return getUserByUsername(username) != null
    }
}