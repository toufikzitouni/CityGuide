package com.city.guide.api.types;

/**
 * Created by toufikzitouni on 14-10-23.
 */
public class ApiResults {
    private ApiResults[] results;
    private String name;
    private double rating;
    private String[] types;
    private ApiResults geometry;
    private ApiResults location;
    private double lat;
    private double lng;
    private String dist;

    public ApiResults[] getResults() {
        return results;
    }

    public void setResults(ApiResults[] results) {
        this.results = results;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public ApiResults getGeometry() {
        return geometry;
    }

    public void setGeometry(ApiResults geometry) {
        this.geometry = geometry;
    }

    public ApiResults getLocation() {
        return location;
    }

    public void setLocation(ApiResults location) {
        this.location = location;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }
}
