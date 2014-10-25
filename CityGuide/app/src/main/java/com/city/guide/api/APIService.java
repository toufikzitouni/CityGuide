package com.city.guide.api;

import retrofit.RestAdapter;

/**
 * Created by toufikzitouni on 14-10-23.
 */
public class APIService {
    public static final String API_KEY = "AIzaSyBOLILuGlSdHg_tfl9I84rS1ZKJBo366a4";
    private static final String API_URL =
            "https://maps.googleapis.com";
    private API api;

    public APIService() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(API_URL).build();

        api = restAdapter.create(API.class);
    }

    public API getApi() {
        return api;
    }
}
