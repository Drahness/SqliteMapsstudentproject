package com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import com.ieseljust.joanciscar.sqlitemapsstudentproject.DBController;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.GoogleFetcherUtils;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.Locales;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.beans.Place;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.beans.Tipos;

import java.util.ArrayList;
import java.util.List;

public class PlaceDAO implements GenericDAO<Place,String> {
    private final SQLiteDatabase db;
    private static SQLiteStatement placeInsertStatement;
    private final Context context;

    public PlaceDAO(Context context) {
        DBController connexion = new DBController(context);
        this.db = connexion.getWritableDatabase();
        this.context = context;
        getCompiledInsert(db);
    }

    public static SQLiteStatement getCompiledInsert(@Nullable SQLiteDatabase db) {
        if(placeInsertStatement == null) {
            placeInsertStatement = db.compileStatement("INSERT INTO Sitios VALUES (?,?,?,?,?,?,?)");
        }
        return placeInsertStatement;
    }

    @Override
    public List<Place> get() {
        Cursor selectAll = db.rawQuery("SELECT * FROM Sitios S",null);
        List<Place> places = new ArrayList<>();
        while(selectAll.moveToNext()) {
            Cursor selecttipos = db.rawQuery("SELECT * FROM TIPOS WHERE place_id = "+selectAll.getString(3),null);
            List<Tipos> tiposList = new ArrayList<>();
            while (selecttipos.moveToNext()) {
                tiposList.add(new TiposDAO(context).getFromCursor(selecttipos));
            }
            Place pl = this.getFromCursor(selectAll);
            pl.setTipos(tiposList.toArray(new Tipos[0]));
            places.add(pl);
        }
        return places;
    }

    public boolean insert(Place obj) {
        int i = 0;
        SQLiteStatement stat = db.compileStatement("INSERT INTO Sitios"+" VALUES (?,?,?,?,?,?,?)");
        SQLiteStatement tipos = db.compileStatement("INSERT INTO Tipos_Sitios VALUES (?,?)");
        stat.bindString(1,obj.getName());
        stat.bindLong(2,obj.getCodi().getCodi());
        stat.bindString(3,obj.getPlace_id());
        stat.bindDouble(4,obj.getLat());
        stat.bindDouble(5,obj.getLon());
        stat.bindString(6,obj.getVecindad());
        stat.bindBlob(7, GoogleFetcherUtils.getBytes(obj.getFoto()));
        i += stat.executeInsert();
        for (Tipos tipo: obj.getTipos()) {
            tipos.bindString(1,obj.getPlace_id());
            tipos.bindString(2,tipo.getGoogle_type());
            i+=tipos.executeUpdateDelete();
        }
        return i > 0;
    }

    @Override
    public boolean update(Place obj, String key) {
        SQLiteStatement stat = db.compileStatement("UPDATE Sitios " +
                "SET name = ?, " +
                "codi = ?," +
                "place_id = ?," +
                "lat = ?," +
                "lon = ?," +
                "vecindad = ?," +
                "foto = ?" +
                "where place_id = ?");
        putInStatement(obj,stat,null);
        stat.bindString(8,key);
        return stat.executeUpdateDelete() > 0;
    }

    @Override
    public boolean delete(String key) {
        SQLiteStatement stat = db.compileStatement("DELETE FROM Sitios WHERE place_id = ?");
        stat.bindString(1,key);
        return stat.executeUpdateDelete() > 0;
    }


    @Override
    public Place get(String key) {
        Cursor selectAll = db.rawQuery("SELECT * FROM Sitios S WHERE "+key,null);
        Place pl = null;
        List<Place> places = new ArrayList<>();
        if(selectAll.moveToNext()) {
            Cursor selecttipos = db.rawQuery("SELECT * FROM Tipos_Sitios WHERE place_id = "+selectAll.getString(3),null);
            List<Tipos> tiposList = new ArrayList<>();
            while (selecttipos.moveToNext()) {
                tiposList.add(new TiposDAO(context).getFromCursor(selecttipos));
            }
            pl = this.getFromCursor(selectAll);
            pl.setTipos(tiposList.toArray(new Tipos[0]));
        }
        return pl;
    }

    @Override
    public List<Place> getWhere(String where) {
        Cursor selectAll = db.rawQuery("SELECT * FROM Sitios S "+where,null);
        List<Place> places = new ArrayList<>();
        while(selectAll.moveToNext()) {
            Cursor selecttipos = db.rawQuery("SELECT * FROM TIPOS WHERE place_id = "+selectAll.getString(3),null);
            List<Tipos> tiposList = new ArrayList<>();
            while (selecttipos.moveToNext()) {
                tiposList.add(new TiposDAO(context).getFromCursor(selecttipos));
            }
            Place pl = this.getFromCursor(selectAll);
            pl.setTipos(tiposList.toArray(new Tipos[0]));
            places.add(pl);
        }
        return places;
    }

    public void putInStatement(Place obj, SQLiteStatement stat, @Nullable Integer integer) {
        if(integer == null) {
            integer = 1;
        }
        stat.bindString(integer,obj.getName());
        stat.bindLong(integer+1,obj.getCodi().getCodi());
        stat.bindString(integer+2,obj.getPlace_id());
        stat.bindDouble(integer+3,obj.getLat());
        stat.bindDouble(integer+4,obj.getLon());
        stat.bindString(integer+5,obj.getVecindad());
        stat.bindBlob(integer+6, GoogleFetcherUtils.getBytes(obj.getFoto()));
    }

    @Override
    public Place getFromCursor(Cursor c) {
        Place pl = new Place();
        pl.setName(c.getString(getColumnIndex(c,"name")));
        pl.setCodi(new PoblacioDAO(context).get(c.getInt(getColumnIndex(c,"codi"))));
        pl.setPlace_id(c.getString(getColumnIndex(c,"place_id")));
        pl.setLat(c.getDouble(getColumnIndex(c,"lat")));
        pl.setLon(c.getDouble(getColumnIndex(c,"lon")));
        pl.setVecindad(c.getString(getColumnIndex(c,"vecindad")));
        pl.setFoto(GoogleFetcherUtils.getBitmap(c.getBlob(getColumnIndex(c,"foto"))));
        return pl;
    }

}
