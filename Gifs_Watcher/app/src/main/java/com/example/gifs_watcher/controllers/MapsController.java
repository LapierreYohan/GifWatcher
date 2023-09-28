package com.example.phonenetworks.controllers;

import com.example.phonenetworks.managers.CacheManager;
import com.example.phonenetworks.models.Record;

import java.util.ArrayList;

public class MapsController {
    private CacheManager cm = null;

    public MapsController() {
        this.cm = CacheManager.getInstance();
    }

    public void setCache(ArrayList<Record> datas) {
        cm.setArray(datas);
    }

    public ArrayList<Record> getCache() {
        return cm.getDatas();
    }
}
