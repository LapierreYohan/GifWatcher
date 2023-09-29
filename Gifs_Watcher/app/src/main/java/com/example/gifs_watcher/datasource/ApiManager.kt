package com.example.gifs_watcher.datasource

import com.example.gifs_watcher.utils.services.TenorService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiManager {
    val BASE_URL = "https://g.tenor.com/"
    private var tenorService: TenorService? = null

    init {
        createRetrofit()
    }
    fun getTenorService(): TenorService? {
        return tenorService
    }

    private fun createRetrofit() {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        tenorService = retrofit.create(TenorService::class.java)
    }
}