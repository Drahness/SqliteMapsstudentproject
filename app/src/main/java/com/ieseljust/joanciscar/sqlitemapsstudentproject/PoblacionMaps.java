package com.ieseljust.joanciscar.sqlitemapsstudentproject;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.PlaceDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.PoblacioDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.TiposDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Poblacio;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Tipos;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.utils.DBController;

import java.util.List;

public class PoblacionMaps extends MainMenu implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener  {

    private GoogleMap mMap;
    private Location myLocation;
    private SupportMapFragment mapFragment;
    private Spinner dropdown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        dropdown = findViewById(R.id.place_types);
        ArrayAdapter<Tipos> adapter = new ArrayAdapter<>(this,R.layout.place_type_dropdown_template,R.id.place_type_dropdown_item,new TiposDAO(new DBController(this)).get().toArray(new Tipos[0]));
        dropdown.setAdapter(adapter);



        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_poblacions);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            // In debug, you need to set a location or will throw an exception,
            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            myLocation = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(new Criteria(), false));
            mapFragment.getMapAsync(this);
        }

        System.out.println();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            // In debug, you need to set a location or will throw an exception,
            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            myLocation = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(new Criteria(), false));
            mapFragment.getMapAsync(this);
        }
    }


    @Override
    protected int getActivityLayout() {
        return R.layout.content_poblacion_maps_mainframe;
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
        if(myLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(),myLocation.getLongitude()),13));
        }

        List<Poblacio> poblaciones = new PoblacioDAO(new DBController(this)).get();
        for (Poblacio pob : poblaciones) {
            LatLng latLng = new LatLng(pob.getLat(),pob.getLon());
            Marker mark = mMap.addMarker(new MarkerOptions().position(latLng).title(pob.getNom()));
            mark.setTag(pob);
        }
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // TODO IMPLEMENTAR EL CAMBIO DE TIPO
        //dropdown
        Tipos type = (Tipos) this.dropdown.getSelectedItem();
        Intent intent = new Intent();
        Poblacio poblacio = (Poblacio) marker.getTag();
        new PlaceDAO(new DBController(this)).getFor(poblacio,type);
        Bundle b = new Bundle();
        b.putSerializable("poblacio",poblacio);
        b.putString("google_type",type.getGoogle_type());
        intent.putExtras(b);
        intent.setClass(this,SitiosEnPoblacion.class);
        this.startActivity(intent);
        return true;
    }
}