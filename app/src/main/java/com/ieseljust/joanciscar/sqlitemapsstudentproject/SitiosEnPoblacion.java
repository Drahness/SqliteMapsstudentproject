package com.ieseljust.joanciscar.sqlitemapsstudentproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.PlaceDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Place;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Poblacio;

import java.io.Serializable;
import java.util.List;

public class SitiosEnPoblacion extends MainMenu implements OnMapReadyCallback {
    private Poblacio pob;
    private String type;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        pob = (Poblacio) getIntent().getExtras().getSerializable("poblacio");
        type = getIntent().getExtras().getString("google_type");


    }

    @Override
    protected int getActivityLayout() {return R.layout.content_sitios_en_poblacion;}

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
        LatLng latLngPoblacion = new LatLng(pob.getLat(),pob.getLon());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngPoblacion,15));
        List<Place> sitios = new PlaceDAO(this).getFor(pob,type);
        Marker mark;
        for (Place sitio : sitios) {
            LatLng sitioPosition = new LatLng(sitio.getLat(),sitio.getLon());
            mark = mMap.addMarker(new MarkerOptions().title(sitio.getName()).position(sitioPosition));
            mark.setTag(sitio);
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent();
                intent.setClass(SitiosEnPoblacion.this,PlaceActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("sitio", (Serializable) marker.getTag());
                startActivity(intent,b);
                return true;
            }
        });
    }
}