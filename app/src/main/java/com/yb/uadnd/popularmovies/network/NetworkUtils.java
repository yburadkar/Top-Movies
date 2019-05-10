package com.yb.uadnd.popularmovies.network;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {

    //Url constants for getting movie posters
    private static final String POSTERS_BASE_URL = "https://image.tmdb.org/t/p";
    private static final String POSTER_SIZE_PATH = "w342";  //w185

    //Url constant for Youtube videos
    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch";
    private static final String PARAM_VIDEO = "v";

    /*Builds the url to retrieve the poster */
    public static URL buildPosterURL(String imagePath){

        URL url = null;

        if( ( imagePath == null) || ( imagePath.length() < 4 ) )
            return null;

        Uri builtUri = Uri.parse(POSTERS_BASE_URL).buildUpon()
                .appendPath(POSTER_SIZE_PATH)
                .appendPath(imagePath)
                .build();

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildYouTubeUrl(String key){
        URL url = null;

        Uri uri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_VIDEO, key)
                .build();

        try {
            url = new URL(uri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

}
