package com.example.gifs_watcher.networks

import com.example.gifs_watcher.networks.services.TenorService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiDatasource {
    private const val BASE_URL = "https://g.tenor.com/"
    private lateinit var tenorService: TenorService

    init {
        createRetrofit()
    }
    fun getTenorService(): TenorService {
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