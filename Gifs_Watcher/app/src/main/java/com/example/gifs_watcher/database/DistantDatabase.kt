package com.example.gifs_watcher.database

import com.example.gifs_watcher.database.services.AuthService
import com.example.gifs_watcher.database.services.FirestoreService
import com.example.gifs_watcher.database.services.StorageService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

object DistantDatabase  {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage : FirebaseStorage = FirebaseStorage.getInstance()

    var authService: AuthService = AuthService(this.auth)
    var FirestoreService: FirestoreService = FirestoreService(this.firestore)
    var StorageService: StorageService = StorageService(this.storage)
}