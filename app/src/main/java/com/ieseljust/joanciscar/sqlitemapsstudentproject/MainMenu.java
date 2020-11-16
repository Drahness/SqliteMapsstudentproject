package com.ieseljust.joanciscar.sqlitemapsstudentproject;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;

public abstract class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewStub vs = findViewById(R.id.viewStub);
        vs.setLayoutResource(getActivityLayout());
        vs.inflate();
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