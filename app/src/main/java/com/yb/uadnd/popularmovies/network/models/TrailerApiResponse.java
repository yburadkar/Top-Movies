package com.yb.uadnd.popularmovies.network.models;

import java.util.List;

public class TrailerApiResponse {
//    private String id;
    private List<Trailer> results;

    public TrailerApiResponse() {
    }

    public TrailerApiResponse(List<Trailer> results) {
//        this.id = id;
        this.results = results;
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}
