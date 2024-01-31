package com.example.gifs_watcher.networks.services

import com.example.gifs_watcher.models.TenorData
import retrofit2.http.GET
import retrofit2.http.Query

interface TenorService {
    @GET("v1/random?key=LIVDSRZULELA&limit=10&contentfilter=high")
    suspend fun getRandomsGifs(
        @Query("key") key: String?,
        @Query("locale") locale: String?,
        @Query("limit") limit: String?,
        @Query("contentfilter") contentFilter: String?,
        @Query("media_filter") mediaFilter: String?,
        @Query("q") q: String?
    ): TenorData?
}