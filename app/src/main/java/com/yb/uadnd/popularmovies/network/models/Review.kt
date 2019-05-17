package com.yb.uadnd.popularmovies.network.models

class Review {
    var author: String? = null
    var content: String? = null

    constructor() {}

    constructor(author: String, content: String) {
        this.author = author
        this.content = content
    }
}
