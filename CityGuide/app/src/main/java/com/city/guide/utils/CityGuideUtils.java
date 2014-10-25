package com.city.guide.utils;

import android.content.Context;
import android.text.TextUtils;

import com.city.guide.api.API;
import com.city.guide.api.APIService;
import com.city.guide.api.types.ApiResults;
import com.city.guide.api.types.DestinationResults;
import com.city.guide.listeners.DataListener;
import com.city.guide.providers.CityGuideDBOps;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by toufikzitouni on 14-10-24.
 */
public class CityGuideUtils {
    private static CityGuideUtils INSTANCE;
    private static final String RADIUS = "500";

    private static API sApi;

    public static CityGuideUtils getInstance() {
        if (INSTANCE == null) {
            sApi = new APIService().getApi();
            INSTANCE = new CityGuideUtils();
        }
        return INSTANCE;
    }

    public void pollResults(final Context context, final DataListener listener) {
        GpsUtils.getInstance(context, new GpsUtils.CurrentLocationListener() {
            @Override
            public void onLocationChanged() {
                final double latitude = GpsUtils.getInstance(context, null).getLatitude();
                final double longitude = GpsUtils.getInstance(context, null).getLongitude();
                sApi.getPlacesNearby(latitude + "," + longitude, RADIUS,
                        APIService.API_KEY, new Callback<ApiResults>() {
                            @Override
                            public void success(ApiResults apiResults, Response response) {
                                CityGuideDBOps.insertResults(context, apiResults);
                                ApiResults[] results = apiResults.getResults();
                                String[] locations = new String[results.length];
                                for (int i = 0; i < results.length; i++) {
                                    locations[i] = results[i].getGeometry().getLocation().getLat() +
                                            "," + results[i].getGeometry().getLocation().getLng();
                                }
                                String locs = TextUtils.join("|", locations);
                                sApi.getDistances(latitude + "," + longitude, locs,
                                        APIService.API_KEY,
                                        new Callback<DestinationResults>() {
                                            @Override
                                            public void success(
                                                    DestinationResults destinationResults,
                                                    Response response) {
                                                CityGuideDBOps.updateDistances(context,
                                                        destinationResults);
                                                listener.onDataReceived();
                                            }

                                            @Override
                                            public void failure(RetrofitError error) {

                                            }
                                        });
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                listener.onDataFailed(error);
                            }
                        });
            }
        });
    }

    public void getResults(final Context context, final DataListener listener) {
        final double latitude = GpsUtils.getInstance(context, null).getLatitude();
        final double longitude = GpsUtils.getInstance(context, null).getLongitude();
        sApi.getPlacesNearby(latitude + "," + longitude, RADIUS,
                APIService.API_KEY, new Callback<ApiResults>() {
                    @Override
                    public void success(ApiResults apiResults, Response response) {
                        CityGuideDBOps.insertResults(context, apiResults);
                        ApiResults[] results = apiResults.getResults();
                        String[] locations = new String[results.length];
                        for (int i = 0; i < results.length; i++) {
                            locations[i] = results[i].getGeometry().getLocation().getLat() +
                                    "," + results[i].getGeometry().getLocation().getLng();
                        }
                        String locs = TextUtils.join("|", locations);
                        sApi.getDistances(latitude + "," + longitude, locs,
                                APIService.API_KEY,
                                new Callback<DestinationResults>() {
                                    @Override
                                    public void success(
                                            DestinationResults destinationResults,
                                            Response response) {
                                        CityGuideDBOps.updateDistances(context,
                                                destinationResults);
                                        listener.onDataReceived();
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {

                                    }
                                });
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        listener.onDataFailed(error);
                    }
                });
    }

    public List<ApiResults> getResults(Context context) {
        return CityGuideDBOps.getData(context);
    }
}
