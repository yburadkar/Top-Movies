package com.yb.uadnd.popularmovies.network.models

class ReviewApiResponse {
    var results: List<Review>? = null

    constructor() {}

    constructor(results: List<Review>) {
        this.results = results
    }
}
