package com.ieseljust.joanciscar.sqlitemapsstudentproject.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Place implements Serializable {
    private Poblacio codi;
    private String name;
    private String place_id;
    private double lat;
    private double lon;
    private String vecindad;
    private Tipos[] tipos;
    private String foto;
    private String phone = null;

    public Place(JSONObject o) throws JSONException, MalformedURLException {
        this.name = o.getString("name");
        JSONArray jsonArray = o.getJSONArray("types");
        List<Tipos> typesOut = new ArrayList<>();
        for (int i = 0; i < jsonArray.length() ; i++) {
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
            foto = jsonObject.getString("photo_reference");
        } else {
            foto = null;
        }
    }
    public Place() {}

    public void setCodi(Poblacio codi) {
        this.codi = codi;
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
    public String getFotoReference() {
        return foto;
    }
    public void setFotoReference(String foto) {
        this.foto = foto;
    }

    public String getPhone() {return this.phone;}
    public void setPhone(String phone) {this.phone = phone;}
}
