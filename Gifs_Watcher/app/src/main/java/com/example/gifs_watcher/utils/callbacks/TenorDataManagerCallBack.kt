package com.example.gifs_watcher.utils.callbacks

import com.example.gifs_watcher.models.TenorData

interface TenorDataManagerCallBack {
    fun getDataResponseSuccess(tenorData : TenorData) : Unit;
    fun getDataResponseError(message : String) : Unit;
}