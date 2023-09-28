package com.example.gifs_watcher.services;


import com.example.gifs_watcher.models.TenorData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TenorService {

    @GET("search/?dataset=sites-2g-3g-4g-france-metropolitaine-mon-reseau-mobile&q=&sort=code_op")
    Call<TenorData> getPaginateDataSorted(@Query("lang") String lang, @Query("rows") String rows, @Query("start") String start);

    @GET("search/?dataset=sites-2g-3g-4g-france-metropolitaine-mon-reseau-mobile&q=")
    Call<TenorData> getPaginateData(@Query("lang") String lang, @Query("rows") String rows, @Query("start") String start);
}
