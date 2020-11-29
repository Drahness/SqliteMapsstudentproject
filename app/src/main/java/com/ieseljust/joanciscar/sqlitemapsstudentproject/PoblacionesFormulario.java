package com.ieseljust.joanciscar.sqlitemapsstudentproject;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.internal.StringResourceValueReader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.PoblacioDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Poblacio;

public class PoblacionesFormulario extends MainMenu implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Location myLocation;
    private Marker clickMark = null;
    private TextView name;
    private TextView codi;
    private TextView lat;
    private TextView lon;
    private TextView radius;
    private TextView oldCode;
    private TextView oldCodeTxt;
    private Button delete;
    private Button update;
    private Button insert;
    private Button cancel;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode + " " + resultCode);
        if (requestCode == 1) { // The code sended by the other activity
            if(resultCode == RESULT_OK) {
                Poblacio pobSeleccionat = (Poblacio) data.getExtras().getSerializable("poblacio");
                //oldCode.setVisibility(View.VISIBLE);
                //oldCodeTxt.setVisibility(View.VISIBLE);
                //delete.setEnabled(true);
                //update.setEnabled(true);
                enable(true);
                oldCode.setText(String.valueOf(pobSeleccionat.getCodi()));
                name.setText(pobSeleccionat.getNom());
                codi.setText(String.valueOf(pobSeleccionat.getCodi()));
                lat.setText(String.valueOf(pobSeleccionat.getLat()));
                lon.setText(String.valueOf(pobSeleccionat.getLon()));
                radius.setText(String.valueOf(pobSeleccionat.getRadius()));
            }
        }
    }
    private void clear() {
        oldCode.setText("");
        name.setText("");
        codi.setText("");
        lat.setText("");
        lon.setText("");
        radius.setText("");
        enable(false);
    }

    private void enable(boolean activate) {
        if(activate) {
            delete.setEnabled(true);
            update.setEnabled(true);
            oldCode.setVisibility(View.VISIBLE);
            oldCodeTxt.setVisibility(View.VISIBLE);
            insert.setVisibility(View.GONE);
            cancel.setVisibility(View.VISIBLE);
        } else {
            delete.setEnabled(false);
            update.setEnabled(false);
            cancel.setVisibility(View.GONE);
            insert.setVisibility(View.VISIBLE);
            oldCode.setVisibility(View.GONE);
            oldCodeTxt.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = findViewById(R.id.name_village);
        codi = findViewById(R.id.village_codi);
        lat = findViewById(R.id.latitude);
        lon = findViewById(R.id.longitude);
        radius = findViewById(R.id.radius_text);
        oldCode = findViewById(R.id.old_code);
        oldCodeTxt = findViewById(R.id.old_code_view);

        delete = findViewById(R.id.delete_button);
        update = findViewById(R.id.update_button);
        insert = findViewById(R.id.insert_button);
        cancel = findViewById(R.id.cancel);


        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        findViewById(R.id.insert_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = name.getText().toString();
                String postal = codi.getText().toString();
                String latitude = lat.getText().toString();
                String longitude = lat.getText().toString();
                String radius_txt = radius.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(PoblacionesFormulario.this);

                double lat = Double.parseDouble(latitude);
                boolean error = false;
                StringBuilder sb = new StringBuilder();
                if (cityName.equals("")) {
                    sb.append("\t- City Name\n");
                    error = true;
                }
                if (postal.equals("")) {
                    sb.append("\t- Postal Code\n");
                    error = true;
                }
                if (latitude.equals("") || Math.abs(lat) > 180) {
                    sb.append("\t- Latitude needs to be between -180 and 180\n");
                    error = true;
                }
                if (longitude.equals("") || Math.abs(lat) > 180) {
                    sb.append("\t- Longitude needs to be between -180 and 180\n");
                    error = true;
                }
                if (radius_txt.equals("")) {
                    sb.append("\t- Radius \n");
                    error = true;
                }
                if (error) {
                    sb.insert(0,"Error, this fields are empty:\n");
                    builder.setTitle("Incomplete fields");
                    builder.setMessage(sb.toString());
                    builder.create().show();
                } else {
                    Poblacio pob = new Poblacio(Integer.parseInt(postal),
                            cityName,
                            Double.parseDouble(latitude),
                            Double.parseDouble(longitude),
                            Integer.parseInt(radius_txt));
                    PoblacioDAO DAO = new PoblacioDAO(PoblacionesFormulario.this);
                    builder.setTitle("Confirmar accion.");
                    builder.setMessage("Quieres añadir la entrada: \n" + pob +" ?");
                    builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                DAO.insert(pob);
                                clear();
                            }
                        }
                    });
                    if(DAO.get(pob.getCodi()) == null) {
                        builder.create().show();
                    }
                    else {
                        builder.setTitle("El poblado ya existe.");
                        builder.setMessage(pob.toString());
                    }

                }
            }
        });
        findViewById(R.id.update_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = name.getText().toString();
                String nuevoPostal = codi.getText().toString();
                String latitude = lat.getText().toString();
                String longitude = lat.getText().toString();
                String radius_txt = radius.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(PoblacionesFormulario.this);
                StringBuilder sb = new StringBuilder();
                boolean error = false;
                sb.append("Error, this fields are empty:\n");
                if (cityName.equals("")) {
                    sb.append("\t- City Name\n");
                    error = true;
                }
                if (nuevoPostal.equals("")) {
                    sb.append("\t- Postal Code\n");
                    error = true;
                }
                if (latitude.equals("")) {
                    sb.append("\t- Latitude\n");
                    error = true;
                }
                if (longitude.equals("")) {
                    sb.append("\t- Longitude\n");
                    error = true;

                }
                if (radius_txt.equals("")) {
                    sb.append("\t- Radius \n");
                    error = true;
                }
                if (error) {
                    builder.setTitle("Incomplete fields");
                    builder.setMessage(sb.toString());
                    builder.create().show();
                } else {
                    PoblacioDAO DAO = new PoblacioDAO(PoblacionesFormulario.this);
                    Poblacio pob = new Poblacio(Integer.parseInt(nuevoPostal),
                            cityName,
                            Double.parseDouble(latitude),
                            Double.parseDouble(longitude),
                            Integer.parseInt(radius_txt));
                    int po = Integer.parseInt(oldCode.getText().toString());
                    if (DAO.get(po) != null) {
                        builder.setTitle("Confirmar accion.");
                        builder.setMessage("Quieres actualizar la entrada: \n" + po + " A " + pob);
                        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    DAO.update(pob, po);
                                    clear();
                                }
                            }
                        });
                        builder.create().show();
                    } else {
                        builder.setTitle("No existe.");
                        builder.setMessage("La ciudad con codigo postal: " + nuevoPostal + " no existe.");
                        builder.create().show();
                    }
                }
            }
        });
        findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postal = codi.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(PoblacionesFormulario.this);
                StringBuilder sb = new StringBuilder();
                if (postal.equals("")) {
                    sb.append("Error, this field is empty:\n");
                    sb.append("\t- Postal Code\n");
                    builder.setTitle("Incomplete field");
                    builder.setMessage(sb.toString());
                    builder.create().show();
                } else {
                    PoblacioDAO DAO = new PoblacioDAO(PoblacionesFormulario.this);
                    int po = Integer.parseInt(postal);
                    Poblacio pob = DAO.get(po);
                    if (pob != null) {
                        builder.setTitle("Confirmar accion.");
                        builder.setMessage("Quieres borrar la entrada: \n" + pob);
                        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    DAO.delete(po);
                                    clear();
                                }
                            }
                        });
                        builder.create().show();
                    } else {
                        builder.setTitle("No existe.");
                        builder.setMessage("La ciudad con codigo postal: " + postal + " no existe.");
                        builder.create().show();
                    }
                }
            }
        });
        findViewById(R.id.list_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PoblacionesFormulario.this,PoblacionsListView.class);
                startActivityForResult(intent,1);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enable(false);
            }
        });
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.content_poblaciones_formulario;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (myLocation != null) {
            LatLng latLngLocation = new LatLng(this.myLocation.getLatitude(), this.myLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngLocation));
        }
        mMap.setMinZoomPreference(9);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (clickMark != null) {
                    clickMark.remove();
                }
                clickMark = mMap.addMarker(new MarkerOptions().position(latLng).title(name.getText().toString() + " " + codi.getText().toString()));
                lat.setText(String.valueOf(latLng.latitude));
                lon.setText(String.valueOf(latLng.longitude));
            }
        });
    }
}