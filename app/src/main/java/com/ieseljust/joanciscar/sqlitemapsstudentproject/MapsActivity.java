package com.ieseljust.joanciscar.sqlitemapsstudentproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.security.acl.Permission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapsActivity extends MainMenu implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location myLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Places.initialize(this, getString(R.string.google_maps_key));

        PlacesClient placesClient = Places.createClient(this);

        List<Place.Field> fields = new ArrayList<>();
        Place.Field[] uns = new Place.Field[]{Place.Field.ADDRESS_COMPONENTS, Place.Field.OPENING_HOURS, Place.Field.PHONE_NUMBER, Place.Field.UTC_OFFSET, Place.Field.WEBSITE_URI};
        Collections.addAll(fields, Place.Field.values());


        for(Place.Field unsupportedField: uns) {
            fields.remove(unsupportedField);
        }

        FindCurrentPlaceRequest.Builder builder = FindCurrentPlaceRequest.builder(fields);

        FindCurrentPlaceRequest request = builder.build();

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
                .getBestProvider(criteria, false));
        Task<FindCurrentPlaceResponse> task = placesClient.findCurrentPlace(request);

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Failed");
                e.printStackTrace();
            }
        });
        task.addOnSuccessListener(new OnSuccessListener<FindCurrentPlaceResponse>() {
            @Override
            public void onSuccess(FindCurrentPlaceResponse findCurrentPlaceResponse) {
                FindCurrentPlaceResponse response = task.getResult();
                System.out.println(response);
                List<PlaceLikelihood> places = response.getPlaceLikelihoods();
                System.out.println(places);
                for (PlaceLikelihood placeLikelihood : places) {
                    Place pl = placeLikelihood.getPlace();
                    soutAll(pl);
                }
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //assert mapFragment != null;
        mapFragment.getMapAsync(this);

    }
    private void soutAll(Place pl) {
        System.out.println("Address: "+pl.getAddress());
        System.out.println("AddressComponents: "+pl.getAddressComponents());
        System.out.println("Attributions: "+pl.getAttributions());
        System.out.println("BusinessStatus: "+pl.getBusinessStatus());
        System.out.println("ID: "+pl.getId());
        System.out.println("LatLng: "+pl.getLatLng());

        System.out.println("Name: "+pl.getName());
        System.out.println("Rating: "+pl.getRating());
        System.out.println("OpeningHours: "+pl.getOpeningHours());
        System.out.println("PriceLevel: "+pl.getPriceLevel());
        System.out.println("IS Open: "+pl.isOpen());
        System.out.println("WebsiteUri: "+pl.getWebsiteUri());




    }
    @Override
    protected int getActivityLayout() {return R.layout.content_maps;}

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        @SuppressLint("MissingPermission")
        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));
        
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        if(myLocation != null) {
            LatLng latLngLocation = new LatLng(this.myLocation.getLatitude(), this.myLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngLocation));
        }
    }

    //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=37.160317,-2.748416&sensor=true&radius=5000&types=pharmacy&key=AIzaSyB9GCrd7kBdpXeFq5QMI--dPYfNwaByWHc
}