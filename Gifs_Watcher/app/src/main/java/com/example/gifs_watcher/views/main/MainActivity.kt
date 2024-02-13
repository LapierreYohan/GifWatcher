package com.example.gifs_watcher.views.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.media.MediaPlayer
import android.os.Build
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
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import pub.devrel.easypermissions.EasyPermissions
import kotlin.math.abs


class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    private var mediaPlayer: MediaPlayer? = null
    private var isMusicPlaying = false

    private var lastUpdate: Long = 0
    private var lastX: Float = 0.0f
    private var lastY: Float = 0.0f
    private var lastZ: Float = 0.0f
    private val shakeThreshold = 2500

    private var flashHandler: Handler? = null
    private var flashRunnable: Runnable? = null

    private lateinit var cameraManager: CameraManager
    private var cameraId: String? = null
    private var isFlashOn = false

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel : MainViewModel by viewModels<MainViewModel>()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.supportActionBar!!.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        mediaPlayer = MediaPlayer.create(this, R.raw.shake_sound_test)
        if (mediaPlayer == null) {
            Timber.e("Impossible de charger le fichier audio.")
        }

        // Vérifier si l'appareil a un flash
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            try {
                val cameraIds = cameraManager.cameraIdList
                if (cameraIds.isNotEmpty()) {
                    cameraId = cameraIds[0] // Utiliser le premier appareil photo avec flash
                }
            } catch (e: CameraAccessException) {
                Timber.e("Cannot access the camera.")
            }
        } else {
            Timber.e("No flash on this device.")
        }

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometer == null) {
            Timber.e("Le capteur d'accéléromètre n'est pas disponible sur cet appareil.")
        }

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

    override fun onResume() {
        super.onResume()

        accelerometer?.also { accel ->
            sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()

        sensorManager.unregisterListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        flashHandler?.removeCallbacks(flashRunnable!!)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastUpdate > 100) {
                val diffTime = currentTime - lastUpdate
                lastUpdate = currentTime

                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val speed = abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000

                if (speed > shakeThreshold && !isMusicPlaying) {
                    // Le téléphone a été secoué
                    Timber.d("Shake detected!")
                    playMusic()
                    flashHandler = Handler(Looper.getMainLooper())
                    flashRunnable = object : Runnable {
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun run() {
                            toggleFlash() // Activer/désactiver le flash
                            flashHandler?.postDelayed(this, 50) // Délai de 2 secondes
                        }
                    }
                    flashHandler?.postDelayed(flashRunnable!!, 0) // Démarrer immédiatement
                }

                lastX = x
                lastY = y
                lastZ = z
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Timber.d("onAccuracyChanged: $accuracy")
    }

    private fun playMusic() {
        if (!mediaPlayer?.isPlaying!!) {
            mediaPlayer!!.start()
            isMusicPlaying = true

            // Ajouter un écouteur pour détecter la fin de la musique
            mediaPlayer!!.setOnCompletionListener {
                // Réinitialiser l'indicateur une fois la musique terminée
                isMusicPlaying = false
                flashHandler?.removeCallbacks(flashRunnable!!)
                cameraManager.setTorchMode(cameraId!!, false)
            }
        }
    }

    private fun toggleFlash() {
        try {
            cameraManager.setTorchMode(cameraId!!, !isFlashOn)
            isFlashOn = !isFlashOn
        } catch (e: CameraAccessException) {
            Timber.e("Cannot access the camera.")
        }
    }
}