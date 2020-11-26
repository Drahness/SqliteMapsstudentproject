package com.ieseljust.joanciscar.sqlitemapsstudentproject.beans;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.TiposDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.GoogleFetcherUtils;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.find.FetchPhoto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Place {
    private Poblacio codi;
    private String name;
    private String place_id;
    private double lat;
    private double lon;
    private String vecindad;
    private Tipos[] tipos;
    private Bitmap foto;

    private FetchPhoto worker;

    public Place(JSONObject o) throws JSONException, MalformedURLException {
        this.name = o.getString("name");
        JSONArray jsonArray = o.getJSONArray("types");
        List<Tipos> typesOut = new ArrayList<>();
        for (int i = 0; i < typesOut.size() ; i++) {
            typesOut.add(new Tipos(jsonArray.getString(i)));
        }
        this.tipos= typesOut.toArray(new Tipos[0]);
        JSONObject jsonObject = o.getJSONObject("geometry").getJSONObject("location");
        System.out.println(jsonObject);
        lat = jsonObject.getDouble("lat");
        lon = jsonObject.getDouble("lng");
        vecindad = o.getString("vicinity");
        place_id = o.getString("place_id");
        if(!o.isNull("photos")) {
            jsonArray = o.getJSONArray("photos");
            jsonObject = jsonArray.getJSONObject(0);
            worker = new FetchPhoto(this);
            worker.execute(GoogleFetcherUtils.getImageURL(jsonObject.getString("photo_reference")));
        } else {
            foto = null;
        }
    }
    public Place() {}

    public void setCodi(Poblacio codi) {
        this.codi = codi;
    }

    @Override
    public String toString() {
        return "Place{" +
                "codi=" + codi +
                ", name='" + name + '\'' +
                ", place_id='" + place_id + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", vecindad='" + vecindad + '\'' +
                ", tipos=" + Arrays.toString(tipos) +
                ", foto=" + foto +
                '}';
    }

    public Poblacio getCodi() {
        return codi;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPlace_id() {
        return place_id;
    }
    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }
    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLon() {
        return lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }
    public String getVecindad() {
        return vecindad;
    }
    public void setVecindad(String vecindad) {
        this.vecindad = vecindad;
    }
    public Tipos[] getTipos() {
        return tipos;
    }
    public void setTipos(Tipos[] tipos) {
        this.tipos = tipos;
    }
    public Bitmap getFoto() {
        return foto;
    }
    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public boolean working() {
        if(worker != null || worker.getStatus() != AsyncTask.Status.FINISHED) {
            return true;
        }
        else {
            worker = null;
            return false;
        }
    }
}
