package com.example.gifs_watcher.views.main

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.gifs_watcher.R
import com.example.gifs_watcher.databinding.ActivityMainBinding
import com.example.gifs_watcher.models.Results
import com.example.gifs_watcher.utils.adapters.FriendsAdapter
import com.example.gifs_watcher.viewmodel.MainViewModel
import com.example.gifswatcher.models.Users
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel : MainViewModel by viewModels<MainViewModel>()
    private var printedGif : Results? = Results()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.supportActionBar!!.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_friends, R.id.navigation_home, R.id.navigation_profil
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        mainViewModel.printedGifLD.observe(this) {
            printedGif = it
            Timber.e(printedGif.toString())
        }

        mainViewModel.getRandomGif(this)
    }
}