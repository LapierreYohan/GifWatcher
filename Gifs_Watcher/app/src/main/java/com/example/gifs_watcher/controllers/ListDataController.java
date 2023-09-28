package com.example.phonenetworks.controllers;

import android.util.Log;

import com.example.phonenetworks.managers.ApiManager;
import com.example.phonenetworks.managers.CacheManager;
import com.example.phonenetworks.managers.NetworkDataManagerCallBack;
import com.example.phonenetworks.models.Networks;
import com.example.phonenetworks.models.Record;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListDataController {

    private final ApiManager apiManager;
    private CacheManager cm = null;

    public ListDataController() {
        apiManager = ApiManager.getInstance();
        this.cm = CacheManager.getInstance();
    }

    public void getSimpleNetworks(int offset, NetworkDataManagerCallBack callBack) {
        Call<Networks> callSimpleNetworks = apiManager.getNetworkService().getPaginateData("fr", "10", offset+"");
        callSimpleNetworks.enqueue(new Callback<Networks>() {
            @Override
            public void onResponse(Call<Networks> call, Response<Networks> response) {
                if (response.isSuccessful()) {
                    Networks network = response.body();
                    Log.e("onResponse", "");
                    callBack.getDataResponseSuccess(network);

                } else {
                    Log.e("onResponse", "Not successfull : " + response.code());
                    callBack.getDataResponseError("Erreur le serveur a repondu status : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Networks> call, Throwable t) {
                Log.e("onFailure", t.getLocalizedMessage());
                callBack.getDataResponseError("Erreur lors de la requete : " + t.getLocalizedMessage());
            }
        });
    }

    public void getNetworksSorted(int offset, NetworkDataManagerCallBack callBack) {
        Call<Networks> callSimpleNetworks = apiManager.getNetworkService().getPaginateDataSorted("fr", "10", offset+"");
        callSimpleNetworks.enqueue(new Callback<Networks>() {
            @Override
            public void onResponse(Call<Networks> call, Response<Networks> response) {
                if (response.isSuccessful()) {
                    Networks network = response.body();
                    Log.e("onResponse", "");
                    callBack.getDataResponseSuccess(network);

                } else {
                    Log.e("onResponse", "Not successfull : " + response.code());
                    callBack.getDataResponseError("Erreur le serveur a repondu status : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Networks> call, Throwable t) {
                Log.e("onFailure", t.getLocalizedMessage());
                callBack.getDataResponseError("Erreur lors de la requete : " + t.getLocalizedMessage());
            }
        });
    }

    public void setCache(ArrayList<Record> datas) {
        cm.setArray(datas);
    }

    public void addCache(ArrayList<Record> datas) {
        cm.addArray(datas);
    }

    public ArrayList<Record> getCache() {
        return cm.getDatas();
    }

}
