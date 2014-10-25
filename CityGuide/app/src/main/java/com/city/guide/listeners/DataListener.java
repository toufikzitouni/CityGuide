package com.city.guide.listeners;

import retrofit.RetrofitError;

/**
 * Created by toufikzitouni on 14-10-24.
 */
public interface DataListener {
    public void onDataReceived();
    public void onDataFailed(RetrofitError error);
}
