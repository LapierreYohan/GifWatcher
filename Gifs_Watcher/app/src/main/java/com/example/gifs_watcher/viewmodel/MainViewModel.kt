package com.example.gifs_watcher.viewmodel


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gifs_watcher.R
import com.example.gifs_watcher.datasource.CacheManager
import com.example.gifs_watcher.models.Results
import com.example.gifs_watcher.repositories.GifRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var gifRepo : GifRepository = GifRepository
    private var cacheManager : CacheManager = CacheManager

    private val data: ArrayList<Results?> = arrayListOf()
    val dataLD: MutableLiveData<ArrayList<Results?>> = MutableLiveData()
    fun getRandomGif(context: Context) {

        val tenorKey : String = context.getString(R.string.tenor_api_key)
        viewModelScope.launch {
            gifRepo.getRandomGif(tenorKey, "10", "high")
                .collect {
                    it?.let { tenorData ->
                        data.addAll(tenorData.results)
                        dataLD.postValue(tenorData.results)
                    }
                }
        }
    }

     fun setCache(data : ArrayList<Results>) : Unit {
        cacheManager.setArray(data);
    }

    fun addCache(data : ArrayList<Results>) : Unit {
        cacheManager.addArray(data);
    }

    fun getCache() : ArrayList<Results> {
        return cacheManager.getData();
    }
}