package com.example.gifs_watcher.database

import android.annotation.SuppressLint
import com.example.gifs_watcher.database.services.AuthService
import com.example.gifs_watcher.database.services.FirestoreService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

object DistantDatabase  {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    @SuppressLint("StaticFieldLeak")
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage : FirebaseStorage = FirebaseStorage.getInstance()

    val authService: AuthService = AuthService(this.auth)
    val firestoreService: FirestoreService = FirestoreService(this.firestore)
}