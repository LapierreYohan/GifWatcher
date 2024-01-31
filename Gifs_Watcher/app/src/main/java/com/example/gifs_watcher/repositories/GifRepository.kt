package com.example.gifs_watcher.repositories

import android.content.Context
import com.example.gifs_watcher.R
import com.example.gifs_watcher.cache.CacheDatasource
import com.example.gifs_watcher.database.DistantDatabaseDatasource
import com.example.gifs_watcher.networks.ApiDatasource
import com.example.gifs_watcher.models.TenorData
import com.example.gifs_watcher.models.Results
import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.models.maps.GifMapper
import com.example.gifs_watcher.models.maps.models.GifMap
import com.example.gifs_watcher.utils.enums.CacheMode
import com.example.gifs_watcher.utils.managers.ThemeManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object GifRepository {

    private const val LIMIT = "20"
    private const val FILTER = "high"
    private const val LANG = "fr_FR"
    private const val MEDIA_FILTER = "minimal"
    private const val NB_GIFS_PER_FETCH = 3

    private val tenorApi : ApiDatasource = ApiDatasource
    private val cache : CacheDatasource = CacheDatasource
    private val database : DistantDatabaseDatasource = DistantDatabaseDatasource

    private val themeManager : ThemeManager = ThemeManager

    suspend fun getRandomGif(context: Context, theme: String = "") : Flow<Results?> = flow {

         val randomData : TenorData?

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

             cache.add(randomData.results)

         } else {
             randomData = TenorData()
             randomData.results.addAll(cache.get())
         }
        try {
             val resultData : Results? = randomData.results.removeAt(0)
             cache.replace(randomData.results)

             emit(resultData)
        } catch (error : Exception) {
            emit(Results())
        }
    }

    suspend fun prepareGifs(context: Context) {

        val randomData : TenorData?

        if (cache.getMode() != CacheMode.RANDOM) {
            cache.switch(CacheMode.RANDOM)
        }

        if (cache.size() < 1) {
            randomData = fetchGif(context, "")
            cache.add(randomData.results)
        }
    }

    private suspend fun fetchGif(context: Context, theme: String): TenorData {

        val randomData = TenorData()
        var currentTheme: String = theme
        val listOfAlreadyUsedTheme = arrayListOf<String>()

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

                if (i % NB_GIFS_PER_FETCH == 0 && theme.isBlank()) {
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

    suspend fun likeGif(gif : GifMap?, type : String = "like") {

        if (gif == null) {
            return
        }

        // Inserer le gif dans la base de données
        database.insertGif(gif)

        // Récuperer l'utilisateur connecté
        val user : User = cache.getAuthUser() ?: User()

        // Ajouter le gif à ses type de likes
        when (type.lowercase()) {
            "like" -> database.likeGif(gif, user.idUsers!!, "likedGifs", "likes")
            "dislike" -> database.likeGif(gif, user.idUsers!!, "dislikedGifs", "dislikes")
            "star" -> database.likeGif(gif, user.idUsers!!, "starredGifs", "stars")
            "share" -> database.likeGif(gif, user.idUsers!!, "sharedGifs", "shares")
        }
    }

    suspend fun setAvatarGif(gif : GifMap?) {

        if (gif == null) {
            return
        }

        val user : User = cache.getAuthUser() ?: User()

        user.lowProfilPicture = gif.tinyUrl
        user.profilPicture = gif.url

        cache.setAuthUser(user)

        database.setAvatarGif(gif, user.idUsers!!)
    }

    fun setSharedGif(gif : Results) : Flow<Boolean> = flow {
        try {
            cache.setSharedGif(gif)
            emit(true)
        } catch (error : Exception) {
            emit(false)
        }
    }

    fun getSharedGif() : Flow<Results?> = flow {
        emit(cache.getSharedGif())
    }

    fun seeGif(gifId : String, gifPrinted : Results?) : Flow<Results?> = flow {

        cache.switch(CacheMode.RANDOM)
        val listOfGifs = cache.get()
        listOfGifs.add(0, gifPrinted)
        cache.replace(listOfGifs)

        database.getGifById(gifId).collect{ gif ->
            if (gif != null) {
                emit(GifMapper.reverseMap(gif))
            }
            emit(null)
        }
    }

    fun getLikedGifs(type: String) : Flow<ArrayList<GifMap>> = flow {
        val listOfGifs = arrayListOf<GifMap>()
        val user : User = cache.getAuthUser() ?: User()

        database.getLikedGifs(user.idUsers!!, type).collect{ likedGifs ->
            for (gif in likedGifs) {
                listOfGifs.add(gif)
            }
            emit(listOfGifs)
        }
    }
}