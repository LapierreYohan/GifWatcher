package com.example.gifs_watcher.viewmodel


import android.content.Context
import android.util.Log
import com.example.gifs_watcher.R
import com.example.gifs_watcher.datasource.ApiManager
import com.example.gifs_watcher.datasource.CacheManager
import com.example.gifs_watcher.utils.callback.TenorDataManagerCallBack
import com.example.gifs_watcher.models.Results
import com.example.gifs_watcher.models.TenorData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel {
    private var apiManager : ApiManager = ApiManager
    private var cacheManager : CacheManager = CacheManager

    fun getRandomGif(context: Context, callBack : TenorDataManagerCallBack) : Unit {

        val tenorKey : String = context.getString(R.string.tenor_api_key)
        var callRandomGifs : Call<TenorData?>? = apiManager.getTenorService()?.getRandomsGifs(tenorKey, "10", "high");

        callRandomGifs?.enqueue(object : Callback<TenorData?> {

            override fun onResponse(call: Call<TenorData?>, response: Response<TenorData?>) {
                if (response.isSuccessful) {
                    var tenorData : TenorData? = response.body();
                    Log.e("onResponse", tenorData.toString());

                    if (tenorData != null) {
                        callBack.getDataResponseSuccess(tenorData)
                    };

                } else {
                    Log.e("onResponse", "Not successfull : " + response.code());
                    callBack.getDataResponseError("Erreur le serveur a repondu status : " + response.code());
                }
            }

            override fun onFailure(call: Call<TenorData?>, t: Throwable) {
                Log.e("onFailure", t.localizedMessage);
                callBack.getDataResponseError("Erreur lors de la requete : " + t.localizedMessage);
            }
        })
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