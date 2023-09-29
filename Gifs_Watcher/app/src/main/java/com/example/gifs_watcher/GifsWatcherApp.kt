package com.example.gifs_watcher

import android.app.Application
import timber.log.Timber

class GifsWatcherApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}