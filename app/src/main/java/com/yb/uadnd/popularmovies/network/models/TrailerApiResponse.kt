package com.yb.uadnd.popularmovies.network.models

class TrailerApiResponse {
    //    private String id;
    //    public String getId() {
    //        return id;
    //    }
    //
    //    public void setId(String id) {
    //        this.id = id;
    //    }

    var results: List<Trailer>? = null

    constructor() {}

    constructor(results: List<Trailer>) {
        //        this.id = id;
        this.results = results
    }
}
