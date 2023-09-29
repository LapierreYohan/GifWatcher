package com.example.gifs_watcher.repositories

import android.content.Context
import android.util.Log
import com.example.gifs_watcher.R
import com.example.gifs_watcher.datasource.RetrofitDataSource
import com.example.gifs_watcher.models.TenorData
import com.example.gifs_watcher.utils.callback.TenorDataManagerCallBack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object GifRepository {

    private var apiManager : RetrofitDataSource = RetrofitDataSource

    fun getRandomGif(key : String, limit : String, filter : String) : Flow<TenorData?>? = flow {

        apiManager.getTenorService()?.getRandomsGifs(key, limit, filter)?.collect() {
            emit(it)
        }


    }

    fun getRandomGifFlow(key : String, limit : String, filter : String) : Flow<TenorData?>? =
        apiManager.getTenorService()?.getRandomsGifs(key, limit, filter)
}