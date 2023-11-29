package com.example.gifs_watcher.datasource

import com.example.gifs_watcher.database.DistantDatabase

object DistantDatabaseDatasource {
    suspend fun login(email : String, password : String) {
        DistantDatabase.authService.login(email, password)
    }

    suspend fun register() {
    }

}