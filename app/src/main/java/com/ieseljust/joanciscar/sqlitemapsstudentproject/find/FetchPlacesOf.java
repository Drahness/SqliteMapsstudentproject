package com.ieseljust.joanciscar.sqlitemapsstudentproject.find;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.PoblacioDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.beans.Place;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.PlaceDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.GoogleFetcherUtils;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.beans.Poblacio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FetchPlacesOf extends AsyncTask<String, JSONObject,Boolean> {
    private List<Integer> codes = new ArrayList<>();
    private List<String> urls = new ArrayList<>();
    private final String google_key;
    private Context context;

    public FetchPlacesOf(String google_key, Context context) {
        super();
        this.google_key = google_key;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... urls) {
        for (int i = 0; i < urls.length; i++) {
            try {
                URL url = new URL(urls[i]);
                JSONObject json = GoogleFetcherUtils.getJSONContent(url);
                json.put("codi",codes.get(i));
                publishProgress(json);
                /*while(!json.isNull("next_page_token")) {
                    json = GoogleFetcherUtils.getJSONContent(getNextURL(json,url.toString()));
                    publishProgress(json);
                }*/
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private URL getNextURL(JSONObject lastJSON,String parentUrl) throws JSONException, MalformedURLException {
        String token = lastJSON.getString("next_page_token");
        parentUrl += "&pageToken="+token;
        return new URL(parentUrl);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(JSONObject... values) {
        super.onProgressUpdate(values);
        PlaceDAO dao = new PlaceDAO(context);
        for (JSONObject val: values) {
            try {
                Place[] places = getPlacesOf(val);
                for (Place pl: places) {
                    while(pl.working());
                    dao.insert(pl);
                }
            } catch (JSONException | MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private Place[] getPlacesOf(JSONObject val) throws JSONException, MalformedURLException {
        JSONArray array = val.getJSONArray("results");
        Place[] places = new Place[array.length()];
        for (int i = 0; i < array.length(); i++) {
            JSONObject json = array.getJSONObject(i);
            places[i] = new Place(json);
            places[i].setCodi(new PoblacioDAO(context).get(val.getInt("codi")));
        }
        return places;
    }
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        urls.clear();
        urls = null;
        codes.clear();
        codes = null;
        context = null;
    }

    @Override
    protected void onCancelled(Boolean aBoolean) {
        super.onCancelled(aBoolean);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    public void execute() {
        this.execute(urls.toArray(new String[0]));
    }

    public void addPlace(int codi, String type, double lat, double lon, int radius) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
                .append("location=").append(lat).append(',').append(lon).append('&')
                .append("sensor=").append(true).append('&')
                .append("radius=").append(radius).append('&')
                .append("types=").append(type).append('&')
                .append("key=").append(google_key);
        urls.add(sb.toString());
        codes.add(codi);
//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=37.160317,-2.748416&sensor=true&radius=5000&types=pharmacy,market&key=AIzaSyB9GCrd7kBdpXeFq5QMI--dPYfNwaByWHc
    }

    public void addPoblacion(Poblacio pob, String type) {
        this.addPlace(pob.getCodi(),type,pob.getLat(),pob.getLon(),pob.getRadius());
    }
}
