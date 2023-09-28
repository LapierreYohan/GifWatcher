package com.example.phonenetworks.managers;

import com.example.phonenetworks.models.Networks;

public interface NetworkDataManagerCallBack {
    void getDataResponseSuccess(Networks network);
    void getDataResponseError(String message);
}
