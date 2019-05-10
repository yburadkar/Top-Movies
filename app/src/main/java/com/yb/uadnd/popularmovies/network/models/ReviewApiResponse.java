package com.yb.uadnd.popularmovies.network.models;

import java.util.List;

public class ReviewApiResponse {
//    private int id;
//    private int page;
//    private int total_pages;
//    private int total_results;
    private List<Review> results;

    public ReviewApiResponse() {
    }

    public ReviewApiResponse(List<Review> results) {
        this.results = results;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }
}
