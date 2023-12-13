package com.example.gifs_watcher.views.splashscreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gifs_watcher.R
import com.example.gifs_watcher.views.splashscreen.modals.LoginModal

class SplashScreenActivity : AppCompatActivity() {

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

    }
}