package com.example.gifs_watcher.repositories

import com.example.gifs_watcher.datasource.RetrofitDataSource
import com.example.gifs_watcher.models.TenorData
import kotlinx.coroutines.flow.Flow


object GifRepository {

    private var apiManager : RetrofitDataSource = RetrofitDataSource

    suspend fun getRandomGif(key : String, limit : String, filter : String) : Flow<TenorData?> {
        return apiManager.getTenorService().getRandomsGifs(key, limit, filter)
    }

}