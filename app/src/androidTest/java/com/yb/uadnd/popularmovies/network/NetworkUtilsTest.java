package com.yb.uadnd.popularmovies.network;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;

import static com.yb.uadnd.popularmovies.network.NetworkUtils.Companion;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class NetworkUtilsTest {

    @Test
    public void checkBuildPosterUrl_forValidUrl(){
        String imagePath = "ziEuG1essDuWuC5lpWUaw1uXY2O.jpg";
        URL url = Companion.buildPosterURL(imagePath);
        String actual = null;
        if(url != null) actual = url.toString();
        String expected = "https://image.tmdb.org/t/p/w342/" + imagePath;
        assertEquals("Generate valid photo url failed: " + actual, expected, actual );
    }

    @Test
    public void checkBuildPosterUrl_forNullImagePath(){
        String imagePath = null;
        URL url = Companion.buildPosterURL(imagePath);
        String actual = null;
        if(url != null) actual = url.toString();
        String expected = null;
        assertEquals("Failed for null image path: " + actual, expected, actual );
    }

    @Test
    public void buildYouTubeUrl_forValidKey(){
        String key = "ziEuG1essDu";
        URL url = Companion.buildYouTubeUrl(key);
        String actual = null;
        if(url != null) actual = url.toString();
        String expected = "https://www.youtube.com/watch?v="+ key;
        assertEquals("Generate You tube url failed: " + actual, expected, actual );
    }

}