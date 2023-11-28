package com.example.gifs_watcher.datasource

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

object DistantDatabaseDatasource {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    init {
        this.auth = Firebase.auth
        this.database = Firebase.database
    }

    fun login() {

    }

    fun register() {

    }

    //... Autre fonction d'interaction avec Firebase

}