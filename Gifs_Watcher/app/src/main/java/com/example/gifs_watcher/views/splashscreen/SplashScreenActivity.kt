package com.example.gifs_watcher.views.splashscreen

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.gifs_watcher.R
import com.example.gifs_watcher.viewmodel.SplashScreenViewModel
import com.example.gifs_watcher.views.splashscreen.bottomsheets.ModalLogin

class SplashScreenActivity : AppCompatActivity() {

    private val splashViewModel : SplashScreenViewModel = SplashScreenViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed(this::showLoginModal, 500)
    }

    private fun showLoginModal() : Unit {
        val loginMenu: ModalLogin = ModalLogin
        loginMenu.view?.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        loginMenu.show(supportFragmentManager, loginMenu.TAG)

    }
}