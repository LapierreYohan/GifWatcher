package com.example.gifs_watcher.repositories

import android.content.Context
import com.example.gifs_watcher.R
import com.example.gifs_watcher.datasource.CacheManager
import com.example.gifs_watcher.datasource.RetrofitDataSource
import com.example.gifs_watcher.models.TenorData
import com.example.gifs_watcher.models.Results
import com.example.gifs_watcher.utils.enums.CacheMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber


object GifRepository {

    private var tenorApi : RetrofitDataSource = RetrofitDataSource
    private var cache : CacheManager = CacheManager

     fun getRandomGif(context: Context, limit : String = "10", filter : String = "high") : Flow<Results?> = flow {

         var randomData : TenorData?

         if (cache.getMode() != CacheMode.RANDOM) {
             cache.switch(CacheMode.RANDOM)
         }

         if (cache.size() < 1) {
             randomData = tenorApi.getTenorService().getRandomsGifs(context.getString(R.string.tenor_api_key), limit, filter)

             cache.add(randomData?.results ?: arrayListOf())

         } else {
             randomData = TenorData()
             randomData.results.addAll(cache.get())
         }
        try {
             var resultData : Results? = randomData?.results?.removeAt(0)
             cache.replace(randomData?.results ?: arrayListOf())

             emit(resultData)
        } catch (error : Exception) {
            emit(Results())
        }
    }

}