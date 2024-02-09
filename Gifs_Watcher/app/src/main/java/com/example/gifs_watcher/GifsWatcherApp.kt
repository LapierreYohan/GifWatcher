package com.example.gifs_watcher

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.FirebaseApp
import timber.log.Timber

class GifsWatcherApp : Application() {
    companion object {
        @JvmStatic
        var isAppInForeground: Boolean = false
            private set
    }
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        // Initialisez FirebaseApp
        FirebaseApp.initializeApp(this)

        // Ajoutez un observateur du cycle de vie de l'application
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver())
    }

    private class AppLifecycleObserver : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun onEnterForeground() {
            Timber.d("App entered foreground")
            isAppInForeground = true
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onEnterBackground() {
            Timber.d("App entered background")
            isAppInForeground = false
        }
    }
}