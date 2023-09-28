package com.example.phonenetworks.services;

import com.example.phonenetworks.models.Networks;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkService {

    @GET("search/?dataset=sites-2g-3g-4g-france-metropolitaine-mon-reseau-mobile&q=&sort=code_op")
    Call<Networks> getPaginateDataSorted(@Query("lang") String lang, @Query("rows") String rows, @Query("start") String start);

    @GET("search/?dataset=sites-2g-3g-4g-france-metropolitaine-mon-reseau-mobile&q=")
    Call<Networks> getPaginateData(@Query("lang") String lang, @Query("rows") String rows, @Query("start") String start);
}
