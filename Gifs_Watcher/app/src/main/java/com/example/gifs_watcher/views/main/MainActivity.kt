package com.example.gifs_watcher.views.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.gifs_watcher.R
import com.example.gifs_watcher.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel : MainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.supportActionBar!!.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_friends, R.id.navigation_home, R.id.navigation_profil
            )
        )

        mainViewModel.friendsRequestNumber.observe(this) { response ->
            response?.let {number ->

                val nav = binding.navView
                val badge = nav.getOrCreateBadge(R.id.navigation_friends)

                if (number > 0) {
                    Timber.e("number: $number")

                    badge.number = number
                    badge.isVisible = true
                } else {
                    badge.number = 0
                    badge.isVisible = false
                }
            }
        }

        mainViewModel.listenFriendsRequest()

        mainViewModel.getRandomGif()

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}