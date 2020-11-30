package com.ieseljust.joanciscar.sqlitemapsstudentproject;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;

import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.PoblacioDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.TiposDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Poblacio;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Tipos;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.utils.DBController;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.utils.Locales;

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
                    if(new DBController(MainMenu.this).firstInit()) {
                        String[] types = MainMenu.this.getResources().getStringArray(R.array.types);
                        TiposDAO tipos = new TiposDAO(new DBController(MainMenu.this));
                        for (String type : types) {
                            tipos.safeInsert(new Tipos(type));
                        }
                        PoblacioDAO pob = new PoblacioDAO(new DBController(MainMenu.this));
                        pob.safeInsert(new Poblacio(46712, "Piles", 38.9408685, -0.1324241, 1000));
                        pob.safeInsert(new Poblacio(46711, "Miramar", 38.9501877, -0.1405837, 1000));
                        pob.safeInsert(new Poblacio(46713, "Bellreguard", 38.9466531, -0.1624822, 2000));
                        pob.safeInsert(new Poblacio(46715, "Alqueria de la comtessa", 38.9367938, -0.1509766, 1000));
                        pob.safeInsert(new Poblacio(46702, "Gandia", 38.96735, -0.1853385, 2000));
                        pob.safeInsert(new Poblacio(46701, "Grao Gandia", 39.007931, -0.1679302, 2500));
                        pob.safeInsert(new Poblacio(46740, "Carcaixent", 39.1214619, -0.4479085, 2000));
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