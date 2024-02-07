package com.example.gifs_watcher.views.splashscreen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.gifs_watcher.BuildConfig
import com.example.gifs_watcher.R
import com.example.gifs_watcher.views.splashscreen.modals.LoginModal
import timber.log.Timber

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private val splashScreenViewModel : SplashScreenViewModel by viewModels<SplashScreenViewModel>()

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
            else -> {
                setContentView(R.layout.activity_splash_screen_dev)
            }
        }

        showLoginModal()
    }

    private fun showLoginModal() {
        val loginMenu = LoginModal

        loginMenu.show(supportFragmentManager, loginMenu.TAG)
        splashScreenViewModel.prepareGifs()
    }
}