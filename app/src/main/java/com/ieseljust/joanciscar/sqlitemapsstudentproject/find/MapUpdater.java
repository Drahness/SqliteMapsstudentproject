package com.ieseljust.joanciscar.sqlitemapsstudentproject.find;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Place;

public class MapUpdater extends AsyncTask<String, Place,Void> {
    private FetchPlacesOf fetcher;
    private GoogleMap mMap;
    public MapUpdater(Context context,GoogleMap mapToUpdate) {
        this.fetcher = new FetchPlacesOf(context);
        this.mMap = mapToUpdate;
    }
    @Override
    protected Void doInBackground(String... strings) {
        return null;
    }
}
