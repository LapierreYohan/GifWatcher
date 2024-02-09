package com.example.gifs_watcher.database.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.gifs_watcher.GifsWatcherApp
import com.example.gifs_watcher.R
import com.example.gifs_watcher.views.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MessagingService : FirebaseMessagingService() {

    private val channelId = "friendRequest_channel"
    private val channelName = "com.example.gifs_watcher.database.services"
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Timber.d("From: ${remoteMessage.from}")
        // Handle data payload of FCM messages.
        if (remoteMessage.notification != null) {
            Timber.d("Message Notification Body: ${remoteMessage.notification!!.body}")
            val title = remoteMessage.notification!!.title!!
            val message = remoteMessage.notification!!.body!!
            val imageUrl = remoteMessage.notification!!.imageUrl

            // Vérifiez si l'application est en premier plan ou en arrière-plan
            if (GifsWatcherApp.isAppInForeground) {
                Timber.d("L'application est en premier plan")
                // L'application est en premier plan, vous pouvez décider de ne pas afficher la notification ici
                generateNotification(title, message, imageUrl)
            } else {
                Timber.d("L'application est en arrière-plan")
                // L'application est en arrière-plan, affichez la notification
                generateNotification(title, message, imageUrl)
            }
        }
    }

    private fun getRemoteView(title : String, message : String) : RemoteViews {
        val remoteView = RemoteViews(packageName, R.layout.friend_notification)

        remoteView.setTextViewText(R.id.friend_notification_name, title)
        remoteView.setTextViewText(R.id.friend_notification_message, message)
        remoteView.setImageViewResource(R.id.friend_notification_image, R.drawable.loutre_notification)

        return remoteView
    }

    private fun generateNotification(title : String, message : String, imageUrl : Uri? = null) {

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
            .setSmallIcon(R.drawable.loutre_notification)


        imageUrl?.let {
            val bitmap = getBitmapFromUri(it)
            builder.setLargeIcon(bitmap)
        }

        val remoteView = getRemoteView(title, message)
        builder.setCustomContentView(remoteView)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager


         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
             notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())
    }

    private fun getBitmapFromUri(imageUri: Uri): Bitmap? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            Timber.e("Erreur de chargement de l'image: $e")
            null
        }
    }
}