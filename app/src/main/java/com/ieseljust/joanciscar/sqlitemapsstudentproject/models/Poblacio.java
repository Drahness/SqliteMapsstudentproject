package com.ieseljust.joanciscar.sqlitemapsstudentproject.models;

import java.io.Serializable;
import java.util.List;

public class Poblacio implements Serializable {
    private int codi;
    private String nom;
    private double lat;
    private double lon;
    private List<Place> lugares;
    private int radius;

    public Poblacio() {}

    public Poblacio(int codi, String nom, double lat, double lon, int radius) {
        this.codi = codi;
        this.nom = nom;
        this.lat = lat;
        this.lon = lon;
        this.radius = radius;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Poblacio poblacio = (Poblacio) o;

        if (codi != poblacio.codi) return false;
        if (Double.compare(poblacio.lat, lat) != 0) return false;
        if (Double.compare(poblacio.lon, lon) != 0) return false;
        if (radius != poblacio.radius) return false;
        if (!nom.equals(poblacio.nom)) return false;
        return lugares.equals(poblacio.lugares);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = codi;
        result = 31 * result + nom.hashCode();
        temp = Double.doubleToLongBits(lat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lon);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + lugares.hashCode();
        result = 31 * result + radius;
        return result;
    }

    @Override
    public String toString() {
        return  nom + ": "+codi + "\nCoords: "+lat+","+lon+"\nRadius:"+radius;
    }

    public int getCodi() {
        return codi;
    }

    public void setCodi(int codi) {
        this.codi = codi;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
