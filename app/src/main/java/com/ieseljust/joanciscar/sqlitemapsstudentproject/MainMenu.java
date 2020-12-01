package com.ieseljust.joanciscar.sqlitemapsstudentproject;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;

import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.PlaceDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.PoblacioDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.TiposDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Poblacio;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Tipos;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.utils.DBController;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.utils.Locales;

import java.security.Policy;
import java.util.List;

public abstract class MainMenu extends AppCompatActivity {
    public static String API_KEY;
    private boolean isRun = false;
    public static Locales locales;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        API_KEY = getString(R.string.google_maps_key);
        setContentView(R.layout.activity_main);
        ViewStub vs = findViewById(R.id.viewStub);
        vs.setLayoutResource(getActivityLayout());
        vs.inflate();
        if(!isRun) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    super.run();
                    locales = Locales.getInstance(MainMenu.this);
                    PoblacioDAO pobDAO = new PoblacioDAO(new DBController(MainMenu.this));
                    TiposDAO tiposDAO = new TiposDAO(new DBController(MainMenu.this));
                    PlaceDAO placeDAO = new PlaceDAO(new DBController(MainMenu.this));
                    if(new DBController(MainMenu.this).firstInit()) {
                        String[] types = MainMenu.this.getResources().getStringArray(R.array.types);
                        for (String type : types) {
                            tiposDAO.safeInsert(new Tipos(type));
                        }
                        pobDAO.safeInsert(new Poblacio(46712, "Piles", 38.9408685, -0.1324241, 1000));
                        pobDAO.safeInsert(new Poblacio(46711, "Miramar", 38.9501877, -0.1405837, 1000));
                        pobDAO.safeInsert(new Poblacio(46713, "Bellreguard", 38.9466531, -0.1624822, 2000));
                        pobDAO.safeInsert(new Poblacio(46715, "Alqueria de la comtessa", 38.9367938, -0.1509766, 1000));
                        pobDAO.safeInsert(new Poblacio(46702, "Gandia", 38.96735, -0.1853385, 2000));
                        pobDAO.safeInsert(new Poblacio(46701, "Grao Gandia", 39.007931, -0.1679302, 2500));
                        pobDAO.safeInsert(new Poblacio(46740, "Carcaixent", 39.1214619, -0.4479085, 2000));
                    }
                    List<Tipos> tipos = tiposDAO.get();
                    List<Poblacio> poblaciones = pobDAO.get();
                    for(int i = 0 ; i < poblaciones.size(); i++) {
                        for (int y = 0 ; y < tipos.size() ; y++) {
                            if(pobDAO.timeToFetch(poblaciones.get(i).getCodi(),tipos.get(y).getGoogle_type())) {
                               AsyncTask task = placeDAO.fetchFor(poblaciones.get(i),tipos.get(y));
                               while(task.getStatus() == AsyncTask.Status.RUNNING);
                            }
                        }
                    }

                }
            };
            t.start();
            isRun = true;
        }

    }

    @LayoutRes
    protected abstract int getActivityLayout();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent i = new Intent();
        if(id == R.id.home_item_toolbar) {
            i.setClass(this,MainActivity.class);
        }
        else if(id == R.id.maps_item_toolbar) {
            i.setClass(this,PoblacionMaps.class);
        }
        else if(id == R.id.management) {
            i.setClass(this,PoblacionesFormulario.class);
        }
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }
}