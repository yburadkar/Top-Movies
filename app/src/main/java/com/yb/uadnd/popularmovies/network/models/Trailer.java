package com.yb.uadnd.popularmovies.network.models;

public class Trailer {
    private String name;
    private String key;
    private String type;
    private String site;

    public Trailer() {
    }

    public Trailer(String name, String key, String type, String site) {
        this.name = name;
        this.key = key;
        this.type = type;
        this.site = site;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
