package com.example.gifs_watcher.repositories

import android.content.Context
import com.example.gifs_watcher.R
import com.example.gifs_watcher.cache.CacheDatasource
import com.example.gifs_watcher.database.DistantDatabaseDatasource
import com.example.gifs_watcher.networks.ApiDatasource
import com.example.gifs_watcher.models.TenorData
import com.example.gifs_watcher.models.Results
import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.models.maps.models.GifMap
import com.example.gifs_watcher.utils.enums.CacheMode
import com.example.gifs_watcher.utils.managers.ThemeManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object GifRepository {

    val LIMIT = "15"
    val FILTER = "high"
    val LANG = "fr_FR"
    val MEDIA_FILTER = "minimal"
    val NB_GIFS_PER_FETCH = 3

    private val tenorApi : ApiDatasource = ApiDatasource
    private val cache : CacheDatasource = CacheDatasource
    private val database : DistantDatabaseDatasource = DistantDatabaseDatasource

    private val themeManager : ThemeManager = ThemeManager

    fun getRandomGif(context: Context, theme: String = "") : Flow<Results?> = flow {

         var randomData : TenorData?

         if (theme == "") {
             cache.clear(CacheMode.SEARCH)
             cache.switch(CacheMode.RANDOM)
         } else {
             cache.clear(CacheMode.RANDOM)
             cache.clear(CacheMode.SEARCH)
             cache.switch(CacheMode.SEARCH)
         }

         if (cache.size() < 1) {
             randomData = fetchGif(context, theme)

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

    suspend fun prepareGifs(context: Context) : Unit {

        var randomData : TenorData?

        if (cache.getMode() != CacheMode.RANDOM) {
            cache.switch(CacheMode.RANDOM)
        }

        if (cache.size() < 1) {
            randomData = fetchGif(context, "")
            cache.add(randomData?.results ?: arrayListOf())
        }
    }

    private suspend fun fetchGif(context: Context, theme: String): TenorData? {

        val randomData = TenorData()
        var currentTheme: String = theme
        var listOfAlreadyUsedTheme = arrayListOf<String>()

        if (theme != "") {
            val fetchedData = tenorApi.getTenorService().getRandomsGifs(
                context.getString(R.string.tenor_api_key),
                LANG,
                LIMIT,
                FILTER,
                MEDIA_FILTER,
                currentTheme
            )

            randomData.results.addAll(fetchedData?.results ?: emptyList())
            return randomData
        } else {

            for (i in 0 until LIMIT.toInt()) {

                if (i % NB_GIFS_PER_FETCH == 0 && theme == "") {
                    currentTheme = themeManager.getRandomTheme(listOfAlreadyUsedTheme)
                    listOfAlreadyUsedTheme.add(currentTheme)
                }

                val remainingGifs = LIMIT.toInt() - i
                val gifsToFetch =
                    if (remainingGifs >= NB_GIFS_PER_FETCH) NB_GIFS_PER_FETCH else remainingGifs

                val fetchedData = tenorApi.getTenorService().getRandomsGifs(
                    context.getString(R.string.tenor_api_key),
                    LANG,
                    gifsToFetch.toString(),
                    FILTER,
                    MEDIA_FILTER,
                    currentTheme
                )

                randomData.results.addAll(fetchedData?.results ?: emptyList())
            }

            for (j in 0 until NB_GIFS_PER_FETCH * 2) {
                randomData.results.shuffle()
            }

            return randomData
        }
    }

    suspend fun likeGif(gif : GifMap, type : String = "like") {

        // Inserer le gif dans la base de données
        database.insertGif(gif)

        // Récuperer l'utilisateur connecté
        var user : User
        user = cache.getAuthUser() ?: User()

        // Ajouter le gif à ses type de likes
        var typeOfLikes = type.lowercase()

        if (typeOfLikes == "like") {
            database.likeGif(gif, user.idUsers!!, "likedGifs", "likes")
        } else if (typeOfLikes == "dislike") {
            database.likeGif(gif, user.idUsers!!, "dislikedGifs", "dislikes")
        } else if (typeOfLikes == "star") {
           database.likeGif(gif, user.idUsers!!, "starredGifs", "star")
        }
    }
}