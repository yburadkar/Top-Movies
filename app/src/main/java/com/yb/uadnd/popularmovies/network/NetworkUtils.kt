package com.yb.uadnd.popularmovies.network

import android.net.Uri
import java.net.MalformedURLException
import java.net.URL

class NetworkUtils {
    companion object {
        //Url constants for getting movie posters
        val POSTERS_BASE_URL = "https://image.tmdb.org/t/p"
        val POSTER_SIZE_PATH = "w342"
        //Url constant for Youtube videos
        val YOUTUBE_BASE_URL = "https://www.youtube.com/watch"
        val PARAM_VIDEO = "v"

        fun buildPosterURL(imagePath: String?): URL? {
            var url: URL? = null
            if((imagePath == null)  || imagePath.length < 4){
                return null
            }
            val builtUri: Uri = Uri.parse(POSTERS_BASE_URL).buildUpon()
                    .appendPath(POSTER_SIZE_PATH)
                    .appendPath(imagePath)
                    .build()
            try {
                url = URL(builtUri.toString())
            }catch (e: MalformedURLException){
                e.printStackTrace()
            }
            return url
        }

        fun buildYouTubeUrl(key: String): URL?{
            var url: URL? = null
            val uri: Uri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                    .appendQueryParameter(PARAM_VIDEO, key)
                    .build()
            try{
                url = URL(uri.toString())
            }catch (e: MalformedURLException){
                e.printStackTrace()
            }
            return url
        }
    }
}