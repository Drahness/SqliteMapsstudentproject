package com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO;

public class Poblacio {
    private int codi;
    private String nom;
    private double lat;
    private double lon;
    
    public Poblacio() {}

    public Poblacio(int codi, String nom, double lat, double lon) {
        this.codi = codi;
        this.nom = nom;
        this.lat = lat;
        this.lon = lon;
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
}
