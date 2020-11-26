package com.ieseljust.joanciscar.sqlitemapsstudentproject.find;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.ieseljust.joanciscar.sqlitemapsstudentproject.GoogleFetcherUtils;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.beans.Place;

import java.io.IOException;
import java.net.URL;

public class FetchPhoto extends AsyncTask<URL,Void, Bitmap> {
    private Place placeOfThePicture;

    public FetchPhoto(Place toDump) {
        super();
        placeOfThePicture = toDump;
    }

    @Override
    protected Bitmap doInBackground(URL... urls) {
        try {
            Bitmap bitmap = GoogleFetcherUtils.getImage(urls[0]);
            placeOfThePicture.setFoto(bitmap);
            return bitmap;
        } catch (IOException e) {
            placeOfThePicture.setFoto(null);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        placeOfThePicture = null;
    }
}
