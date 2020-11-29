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
