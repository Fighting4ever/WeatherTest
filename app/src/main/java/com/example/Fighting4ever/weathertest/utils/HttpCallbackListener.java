package com.example.Fighting4ever.weathertest.utils;

/**
 * Created by Fighting4Ever on 2016/4/18.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
