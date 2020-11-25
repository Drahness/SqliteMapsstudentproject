package com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ieseljust.joanciscar.sqlitemapsstudentproject.DBController;

import java.util.List;

public class PoblacioDAO implements GenericDAO<Poblacio,Integer> {
    public DBController connexion;
    public SQLiteDatabase db;

    public PoblacioDAO(Context context) {
        this.connexion = new DBController(context);
    }

    /**
     *      URL url = new URL("http://www.something.com/");
     *       //Retrieving the contents of the specified page
     *       Scanner sc = new Scanner(url.openStream());
     * @return
     */
    @Override
    public List<Poblacio> get() {
        return null;
    }

    @Override
    public boolean insert() {
        return false;
    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public Poblacio get(Integer key) {
        return null;
    }
}
