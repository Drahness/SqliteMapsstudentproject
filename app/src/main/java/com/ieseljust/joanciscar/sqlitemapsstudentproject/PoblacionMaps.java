package com.ieseljust.joanciscar.sqlitemapsstudentproject;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.PlaceDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.PoblacioDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Place;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Poblacio;

import java.util.List;

public class PoblacionMaps extends MainMenu implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener  {

    private GoogleMap mMap;
    private Location myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
            return;
        } else {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        }
        myLocation = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(new Criteria(), false));
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_poblacion_maps;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
         */
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(myLocation.getLatitude(),myLocation.getLongitude())));
        List<Poblacio> poblaciones = new PoblacioDAO(this).get();
        for (Poblacio pob : poblaciones) {
            LatLng latLng = new LatLng(pob.getLat(),pob.getLon());
            Marker mark = mMap.addMarker(new MarkerOptions().position(latLng).title(pob.getNom()));
            mark.setTag(pob);
        }
        mMap.setMinZoomPreference(9);
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // TODO IMPLEMENTAR EL CAMBIO DE TIPO
        String type = "pharmacy";
        Intent intent = new Intent();
        Poblacio poblacio = (Poblacio) marker.getTag();
        new PlaceDAO(this).getFor(poblacio,type);
        Bundle b = new Bundle();
        b.putSerializable("poblacio",poblacio);
        b.putString("google_type",type);
        intent.putExtras(b);
        intent.setClass(this,SitiosEnPoblacion.class);
        this.startActivity(intent);
        return true;
    }
}