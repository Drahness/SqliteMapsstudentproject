package com.ieseljust.joanciscar.sqlitemapsstudentproject;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Place;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.utils.GoogleFetcherUtils;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class PlaceActivity extends MainMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Place place = (Place) getIntent().getExtras().getSerializable("sitio");
        TextView namePlace = findViewById(R.id.namePlace);
        TextView vecinityPlace = findViewById(R.id.vecinityPlace);
        TextView phoneNumber = findViewById(R.id.phoneNumber);
        ImageView image = findViewById(R.id.image);

        AsyncTask<String,Bitmap,Void> asyn = new AsyncTask<String,Bitmap,Void>() {

            @Override
            protected Void doInBackground(String... strings) {
                try {
                    URL url = GoogleFetcherUtils.getImageURL(strings[0]);
                    publishProgress(GoogleFetcherUtils.getImage(url));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Bitmap... values) {
                super.onProgressUpdate(values);
                image.setImageBitmap(values[0]);
            }
        };
        if(place != null) {
            asyn.execute(place.getFotoReference());
            namePlace.setText(place.getName());
            namePlace.setText(place.getVecindad());


        }
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.content_place_activity;
    }
}