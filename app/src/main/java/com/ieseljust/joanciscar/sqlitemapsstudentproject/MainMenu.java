package com.ieseljust.joanciscar.sqlitemapsstudentproject;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;

import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.PlaceDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.PoblacioDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.TiposDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.find.Tasker;

public abstract class MainMenu extends AppCompatActivity {
    public static DBController instance;
    public static String API_KEY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        API_KEY = getString(R.string.google_maps_key);
        setContentView(R.layout.activity_main);
        ViewStub vs = findViewById(R.id.viewStub);
        vs.setLayoutResource(getActivityLayout());
        vs.inflate();
        instance = new DBController(MainMenu.this);
        instance.getWritableDatabase();
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();
            }
        };
        t.start();

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
            i.setClass(this,MapsActivity.class);
        }
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }
}