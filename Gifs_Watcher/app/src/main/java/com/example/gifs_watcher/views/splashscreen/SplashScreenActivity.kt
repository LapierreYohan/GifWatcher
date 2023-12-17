package com.example.gifs_watcher.views.splashscreen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.gifs_watcher.R
import com.example.gifs_watcher.views.main.MainViewModel
import com.example.gifs_watcher.views.splashscreen.modals.LoginModal

class SplashScreenActivity : AppCompatActivity() {

    private val splashScreenViewModel : SplashScreenViewModel by viewModels<SplashScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_splash_screen)

        showLoginModal()
    }

    private fun showLoginModal() : Unit {
        val loginMenu: LoginModal = LoginModal

        loginMenu.show(supportFragmentManager, loginMenu.TAG)
        splashScreenViewModel.prepareGifs(applicationContext)
    }
}