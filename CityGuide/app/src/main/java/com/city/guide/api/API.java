package com.city.guide.api;

import com.city.guide.api.types.ApiResults;
import com.city.guide.api.types.DestinationResults;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by toufikzitouni on 14-10-23.
 */
public interface API {

    @GET("/maps/api/place/nearbysearch/json")
    void getPlacesNearby(@Query("location") String location,
                         @Query("radius") String radius,
                         @Query("key") String key,
                         Callback<ApiResults> result);

    @GET("/maps/api/distancematrix/json")
    void getDistances(@Query("origins") String origins,
                         @Query("destinations") String destinations,
                         @Query("key") String key,
                         Callback<DestinationResults> result);
}
