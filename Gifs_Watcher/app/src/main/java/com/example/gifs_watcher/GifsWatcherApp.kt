package com.example.gifs_watcher

import android.app.Application
import com.example.gifs_watcher.database.AppDatabase
import timber.log.Timber

class GifsWatcherApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        AppDatabase.initDatabase(context = applicationContext)
    }
}