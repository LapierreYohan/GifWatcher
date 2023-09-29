package com.example.gifs_watcher.utils.services

import com.example.gifs_watcher.models.TenorData
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TenorService {
    @GET("/v1/random?key=LIVDSRZULELA&limit=10&contentfilter=high")
    fun getRandomsGifs(
        @Query("key") key: String?,
        @Query("limit") limit: String?,
        @Query("contentfilter") contentfilter: String?
    ): Flow<TenorData?>
}