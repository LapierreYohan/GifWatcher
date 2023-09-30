package com.example.gifs_watcher.repositories

import com.example.gifs_watcher.datasource.RetrofitDataSource
import com.example.gifs_watcher.models.TenorData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


object GifRepository {

    private var apiManager : RetrofitDataSource = RetrofitDataSource

     fun getRandomGif(key : String, limit : String, filter : String) : Flow<TenorData?> = flow {
        emit(apiManager.getTenorService().getRandomsGifs(key, limit, filter))
    }

}