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
            Tipos tipo = new Tipos(jsonArray.getString(i));
            if(tipo.getLocal_type() != null) {
                typesOut.add(tipo);
            }
        }
        this.tipos= typesOut.toArray(new Tipos[0]);
        JSONObject jsonObject = o.getJSONObject("geometry").getJSONObject("location");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Place place = (Place) o;

        if (Double.compare(place.lat, lat) != 0) return false;
        if (Double.compare(place.lon, lon) != 0) return false;
        if (!codi.equals(place.codi)) return false;
        if (!name.equals(place.name)) return false;
        if (!place_id.equals(place.place_id)) return false;
        if (vecindad != null ? !vecindad.equals(place.vecindad) : place.vecindad != null)
            return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(tipos, place.tipos)) return false;
        if (foto != null ? !foto.equals(place.foto) : place.foto != null) return false;
        return phone != null ? phone.equals(place.phone) : place.phone == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = codi.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + place_id.hashCode();
        temp = Double.doubleToLongBits(lat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lon);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (vecindad != null ? vecindad.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(tipos);
        result = 31 * result + (foto != null ? foto.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        return result;
    }

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
