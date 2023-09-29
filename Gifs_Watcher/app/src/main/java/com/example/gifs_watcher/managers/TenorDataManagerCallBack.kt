package com.example.gifs_watcher.managers

import com.example.gifs_watcher.models.TenorData

interface TenorDataManagerCallBack {
    fun getDataResponseSuccess(tenorData : TenorData) : Unit;
    fun getDataResponseError(message : String) : Unit;
}