package com.example.phonenetworks.managers;

import com.example.phonenetworks.models.Record;

import java.util.ArrayList;

public class CacheManager {

    private static CacheManager instance;

    private ArrayList<Record> datas = null;

    public static CacheManager getInstance() {
        if (instance == null) {
            instance = new CacheManager();
        }

        return instance;
    }

    private CacheManager() {
        this.datas = new ArrayList<>();
    };

    public void setArray(ArrayList<Record> datas) {
        this.datas = datas;
    }

    public void addArray(ArrayList<Record> datas) {
        this.datas.addAll(datas);
    }

    public ArrayList<Record> getDatas() { return this.datas; }
}
