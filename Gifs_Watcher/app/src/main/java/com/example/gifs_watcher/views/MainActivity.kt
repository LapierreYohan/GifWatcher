package com.example.gifs_watcher.views

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.gifs_watcher.R
import com.example.gifs_watcher.viewmodel.MainViewModel
import com.example.gifs_watcher.databinding.ActivityMainBinding
import com.example.gifs_watcher.utils.callback.TenorDataManagerCallBack
import com.example.gifs_watcher.models.Results
import com.example.gifs_watcher.models.TenorData
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel : MainViewModel = MainViewModel()
    private val data: ArrayList<Results> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        getGifsData()
    }

    private fun getGifsData() {
        mainViewModel.getRandomGif(this, object : TenorDataManagerCallBack {
            override fun getDataResponseSuccess(tenorData: TenorData) {
                val resultsToAdd = tenorData.results ?: emptyList()
                data.addAll(resultsToAdd)
            }

            override fun getDataResponseError(message: String) {
                Toast.makeText(
                    applicationContext,
                    "Une erreur est apparue lors de la recherche de données",
                    Toast.LENGTH_LONG
                )
                Log.e("MainActivity", "Une erreur est apparue lors de la recherche de données")
            }
        })
    }
}