package com.yb.uadnd.popularmovies.network.models

class Trailer {
    var name: String? = null
    var key: String? = null
    var type: String? = null
    var site: String? = null

    constructor() {}

    constructor(name: String, key: String, type: String, site: String) {
        this.name = name
        this.key = key
        this.type = type
        this.site = site
    }
}
