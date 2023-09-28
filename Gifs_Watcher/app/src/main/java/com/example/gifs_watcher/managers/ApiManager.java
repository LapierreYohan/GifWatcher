package com.example.phonenetworks.managers;

import com.example.phonenetworks.services.NetworkService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    final static String BASE_URL = "https://public.opendatasoft.com/api/records/1.0/";
    private NetworkService networkService = null;
    private static ApiManager instance;

    public NetworkService getNetworkService() {
        return networkService;
    }

    public static ApiManager getInstance() {
        if (instance == null) {
            instance = new ApiManager();
        }

        return instance;
    }

    private ApiManager() {
        createRetrofitNetWork();
    }

    private void createRetrofitNetWork() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        Retrofit retrofitClock = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.networkService = retrofitClock.create(NetworkService.class);
    }


}
