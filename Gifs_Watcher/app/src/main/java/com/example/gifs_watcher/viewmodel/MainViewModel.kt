package com.example.gifs_watcher.viewmodel


import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gifs_watcher.R
import com.example.gifs_watcher.datasource.RetrofitDataSource
import com.example.gifs_watcher.datasource.CacheManager
import com.example.gifs_watcher.utils.callback.TenorDataManagerCallBack
import com.example.gifs_watcher.models.Results
import com.example.gifs_watcher.models.TenorData
import com.example.gifs_watcher.repositories.GifRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private var gifRepo : GifRepository = GifRepository
    private var cacheManager : CacheManager = CacheManager

    private val data: ArrayList<Results> = arrayListOf()
    val dataLD: MutableLiveData<ArrayList<Results>> = MutableLiveData()
    fun getRandomGif(context: Context, callBack : TenorDataManagerCallBack) : Unit {

        val tenorKey : String = context.getString(R.string.tenor_api_key)
        viewModelScope.launch {
            gifRepo.getRandomGif(tenorKey, "10", "high")
                ?.collect {
                    it?.let { tenirData->
                        data.addAll(tenirData.results)
                        dataLD.postValue(tenirData.results)
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