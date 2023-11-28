package com.example.gifs_watcher.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gifs_watcher.database.dao.UserDao
import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.utils.Converters

@Database(entities = [User::class], version = 1)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        private lateinit var instance: LocalDatabase
        fun initDatabase(context: Context) {
            this.instance = Room.databaseBuilder(
                context,
                LocalDatabase::class.java,
                "GifWatcher"
            ).build()
        }

        fun getInstance(): LocalDatabase {
            return instance
        }
    }
}