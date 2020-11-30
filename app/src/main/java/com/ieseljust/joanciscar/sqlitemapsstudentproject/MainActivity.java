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
    }

    @Override
    protected int getActivityLayout() {return R.layout.content_main;}
}
