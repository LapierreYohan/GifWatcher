package com.example.gifs_watcher.database.services

import com.google.firebase.auth.FirebaseAuth

class AuthService {

    private lateinit var auth: FirebaseAuth

    constructor(auth: FirebaseAuth) {
        this.auth = auth
    }

    suspend fun login(email : String, password : String) {

    }
    suspend fun register() {

    }
}