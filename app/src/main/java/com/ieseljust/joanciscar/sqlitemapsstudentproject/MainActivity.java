package com.ieseljust.joanciscar.sqlitemapsstudentproject;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.PoblacioDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Poblacio;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.find.FetchPlacesOf;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MainMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView txt = findViewById(R.id.textView123);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PoblacioDAO pob = new PoblacioDAO(MainActivity.this);
                try {
                    pob.insert(new Poblacio(46712, "Piles", 38.9408685, -0.1324241, 1000));
                    pob.insert(new Poblacio(46711, "Miramar", 38.9501877, -0.1405837, 1000));
                    pob.insert(new Poblacio(46713, "Bellreguard", 38.9466531, -0.1624822, 2000));
                    pob.insert(new Poblacio(46715, "Alqueria de la comtessa", 38.9367938, -0.1509766, 1000));
                    pob.insert(new Poblacio(46702, "Gandia", 38.96735, -0.1853385, 5000));
                    pob.insert(new Poblacio(46701, "Grao Gandia", 39.007931, -0.1679302, 5000));
                    pob.insert(new Poblacio(46740, "Carcaixent", 39.1214619, -0.4479085, 2000));
                } catch(SQLiteConstraintException ex) {
                    pob.delete(46712);
                    pob.delete(46711);
                    pob.delete(46713);
                    pob.delete(46715);
                    pob.delete(46702);
                    pob.delete(46701);
                    pob.delete(46740);
                    pob.insert(new Poblacio(46712, "Piles", 38.9408685, -0.1324241, 1000));
                    pob.insert(new Poblacio(46711, "Miramar", 38.9501877, -0.1405837, 1000));
                    pob.insert(new Poblacio(46713, "Bellreguard", 38.9466531, -0.1624822, 2000));
                    pob.insert(new Poblacio(46715, "Alqueria de la comtessa", 38.9367938, -0.1509766, 1000));
                    pob.insert(new Poblacio(46702, "Gandia", 38.96735, -0.1853385, 2000));
                    pob.insert(new Poblacio(46701, "Grao Gandia", 39.007931, -0.1679302, 2500));
                    pob.insert(new Poblacio(46740, "Carcaixent", 39.1214619, -0.4479085, 2000));
                }
                List<Poblacio> poblacions = pob.get();
                List<JSONObject> listOfObj = new ArrayList<>();
                FetchPlacesOf task = new FetchPlacesOf(MainActivity.this);
                for (Poblacio poblacion: poblacions) {
                    task.addPoblacion(poblacion,"pharmacy");
                }
                task.execute();
                txt.setText(pob.get().toString().replace("[","").replace("]",""));
            }
        });
    }

    @Override
    protected int getActivityLayout() {return R.layout.content_main;}
}
