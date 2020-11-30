package com.ieseljust.joanciscar.sqlitemapsstudentproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.PlaceDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Place;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.utils.DBController;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.utils.GoogleFetcherUtils;

import org.json.JSONException;
import org.json.JSONObject;
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
        phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+((TextView) v).getText().toString().trim()));
                startActivity(intent);
            }
        });
        ImageView image = findViewById(R.id.image);

        if(place != null) {
            if(place.getPhone() == null) {
                AsyncTask<String, String, Void> asynPhone = new AsyncTask<String, String, Void>() {
                    @Override
                    protected Void doInBackground(String... strings) {
                        try {
                            URL url = GoogleFetcherUtils.getPlaceDetailsURL(strings[0], "formatted_phone_number");
                            publishProgress(GoogleFetcherUtils.getResults(url).getString("formatted_phone_number"));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(String... values) {
                        super.onProgressUpdate(values);
                        phoneNumber.setEnabled(true);
                        phoneNumber.setText(values[0]);
                        place.setPhone(values[0]);
                        new PlaceDAO(new DBController(PlaceActivity.this)).update(place, place.getPlace_id());
                    }
                };
                asynPhone.execute(place.getPlace_id());
            }
            AsyncTask<String, Bitmap, Void> asynImage = new AsyncTask<String, Bitmap, Void>() {
                @Override
                protected Void doInBackground(String... strings) {

                    try {
                        URL url = GoogleFetcherUtils.getImageURL(strings[0], 2000);
                        publishProgress(GoogleFetcherUtils.getImage(url));
                        return null;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        publishProgress(GoogleFetcherUtils.getImage(new URL("https://i.redd.it/3k7uvh8rz5k41.jpg")));
                        return null;
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
            asynImage.execute(place.getFotoReference());
            namePlace.setText(place.getName());
            vecinityPlace.setText(place.getVecindad());


        }
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.content_place_activity;
    }
}