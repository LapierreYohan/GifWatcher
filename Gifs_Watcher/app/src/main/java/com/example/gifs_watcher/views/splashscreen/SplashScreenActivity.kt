package com.example.gifs_watcher.views.splashscreen

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.gifs_watcher.BuildConfig
import com.example.gifs_watcher.R
import com.example.gifs_watcher.views.main.MainActivity
import com.example.gifs_watcher.views.splashscreen.modals.LoginModal
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private val splashScreenViewModel : SplashScreenViewModel by viewModels<SplashScreenViewModel>()

    private val closeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            this.supportActionBar!!.hide()
        } catch (_: NullPointerException) {
        }

        when (BuildConfig.FLAVOR) {
            "develop" -> {
                setContentView(R.layout.activity_splash_screen_dev)
            }
            "production" -> {
                setContentView(R.layout.activity_splash_screen_prod)
            }
            "recette" -> {
                setContentView(R.layout.activity_splash_screen_recette)
            }
            else -> {
                setContentView(R.layout.activity_splash_screen_dev)
            }
        }

        val filter = IntentFilter("ACTION_CLOSE_ACTIVITY")
        registerReceiver(closeReceiver, filter, RECEIVER_NOT_EXPORTED)

        splashScreenViewModel.resetLoginDetails()

        splashScreenViewModel.currentAuth.observe(this) {
            if (it != null) {
                Timber.d("User is logged in")
                Timber.d("User is ${it.username}")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Timber.d("User is not logged in")
                showLoginModal()
            }
        }

        splashScreenViewModel.getLoggedUser()
    }

    private fun showLoginModal() {
        val loginMenu = LoginModal()

        loginMenu.show(supportFragmentManager, loginMenu.TAG)
        splashScreenViewModel.prepareGifs()
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(closeReceiver)
    }
}