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

    private var printedGif: Results? = Results()
    val printedGifLD: MutableLiveData<Results?> = MutableLiveData()
    fun getRandomGif(context: Context) {

        viewModelScope.launch {
            gifRepo.getRandomGif(context)
                .collect {
                    it?.let { tenorData ->
                        printedGif = tenorData
                        printedGifLD.postValue(tenorData)
                    }
                }
        }
    }
}