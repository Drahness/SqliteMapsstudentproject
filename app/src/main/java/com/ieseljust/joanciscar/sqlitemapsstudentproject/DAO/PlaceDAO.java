package com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ieseljust.joanciscar.sqlitemapsstudentproject.find.FetchPlacesOf;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Poblacio;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.utils.DBController;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Place;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Tipos;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.utils.Locales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaceDAO extends ViewModel implements GenericDAO<Place,String> {
    //TODO remove db?
    private final SQLiteDatabase db;
    private final SQLiteOpenHelper connexion;
    private static SQLiteStatement placeInsertStatement;

    public PlaceDAO(SQLiteOpenHelper sqLiteOpenHelper) {
        this.connexion = sqLiteOpenHelper;
        this.db = connexion.getWritableDatabase();
    }

    @Override
    public List<Place> get() {
        Cursor selectAll = db.rawQuery("SELECT * FROM Sitios S",null);
        List<Place> places = new ArrayList<>();
        while(selectAll.moveToNext()) {
            Cursor selecttipos = db.rawQuery("SELECT * FROM TIPOS WHERE place_id = "+selectAll.getString(3),null);
            List<Tipos> tiposList = new ArrayList<>();
            while (selecttipos.moveToNext()) {
                tiposList.add(new TiposDAO(connexion).getFromCursor(selecttipos));
            }
            Place pl = this.getFromCursor(selectAll);
            pl.setTipos(tiposList.toArray(new Tipos[0]));
            places.add(pl);
        }
        return places;
    }

    public boolean insert(Place obj) {
        int i = 0;
        TiposDAO tiposDAO = new TiposDAO(connexion);
        db.beginTransaction();
        SQLiteStatement stat = db.compileStatement("INSERT INTO Sitios"+" VALUES (?,?,?,?,?,?,?,?)");
        SQLiteStatement tipos = db.compileStatement("INSERT INTO Tipos_Sitios VALUES (?,?)");
        stat.bindString(1,obj.getName());
        stat.bindString(2,obj.getPlace_id());
        stat.bindLong(3,obj.getCodi().getCodi());
        stat.bindDouble(4,obj.getLat());
        stat.bindDouble(5,obj.getLon());
        stat.bindString(6,obj.getVecindad());
        if(obj.getFotoReference() != null) {
            stat.bindString(7, obj.getFotoReference());
        } else {
            stat.bindNull(7);
        }
        i += stat.executeInsert();
        for (Tipos tipo: obj.getTipos()) {
            tipos.bindString(1,obj.getPlace_id());
            tipos.bindString(2,tipo.getGoogle_type());
            try {
                i += tipos.executeInsert();
            } catch (SQLiteConstraintException ex) {
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return i > 0;
    }
    // TODO to interface
    public boolean insertOrUpdate(Place obj) {
        Place existentInstance = this.get(obj.getPlace_id());
        if(existentInstance == null) {
            return this.insert(obj);
        } else {
            return this.update(obj,obj.getPlace_id());
        }
    }
    @Override
    public boolean update(Place obj, String key) {
        SQLiteStatement stat = db.compileStatement("UPDATE Sitios " +
                "SET name = ?, " +
                "place_id = ?," +
                "codi = ?," +
                "lat = ?," +
                "lon = ?," +
                "vecindad = ?," +
                "foto = ?," +
                "telefono = ?" +
                "where place_id = ?");
        SQLiteStatement updates = db.compileStatement("UPDATE Tipos_Sitios SET place_id = ?, google_type = ? WHERE place_id = ?");
        for (int i = 0; i < obj.getTipos().length; i++) {
            updates.bindString(1,obj.getPlace_id());
            updates.bindString(2,obj.getTipos()[i].getGoogle_type());
            updates.executeUpdateDelete();
        }
        putInStatement(obj,stat,null);
        stat.bindString(8,key);
        return stat.executeUpdateDelete() > 0;
    }

    @Override
    public boolean delete(String key) {
        SQLiteStatement stat = db.compileStatement("DELETE FROM Sitios WHERE place_id = ?");
        SQLiteStatement del = db.compileStatement("DELETE FROM Tipos_Sitios WHERE place_id = ?");
        stat.bindString(1,key);
        del.bindString(1,key);
        return stat.executeUpdateDelete() + del.executeUpdateDelete() > 0;
    }


    @Override
    public Place get(String key) {
        Cursor selectAll = db.rawQuery("SELECT * FROM Sitios WHERE place_id = '"+key+"'",null);
        Place pl = null;
        List<Place> places = new ArrayList<>();
        if(selectAll.moveToNext()) {
            Cursor selecttipos = db.rawQuery("SELECT * FROM Tipos_Sitios WHERE place_id = '"+key+"'",null);
            List<Tipos> tiposList = new ArrayList<>();
            while (selecttipos.moveToNext()) {
                tiposList.add(new TiposDAO(connexion).get(selecttipos.getString(this.getColumnIndex(selecttipos,"google_type"))));
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
            Cursor selecttipos = db.rawQuery("SELECT * FROM TIPOS WHERE place_id = "+"'"+selectAll.getString(3)+"'",null);
            List<Tipos> tiposList = new ArrayList<>();
            while (selecttipos.moveToNext()) {
                tiposList.add(new TiposDAO(connexion).getFromCursor(selecttipos));
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
        stat.bindString(integer+1,obj.getPlace_id());
        stat.bindLong(integer+2,obj.getCodi().getCodi());
        stat.bindDouble(integer+3,obj.getLat());
        stat.bindDouble(integer+4,obj.getLon());
        stat.bindString(integer+5,obj.getVecindad());
        if( obj.getFotoReference() != null) {
            stat.bindString(integer + 6, obj.getFotoReference());
        } else {
            stat.bindNull(integer +6);
        }
        if(obj.getPhone() != null) {
            stat.bindString(integer + 7,obj.getPhone());
        } else {
            stat.bindNull(integer + 7);
        }
    }

    @Override
    public Place getFromCursor(Cursor c) {
        Place pl = new Place();
        pl.setName(c.getString(getColumnIndex(c,"name")));
        pl.setCodi(new PoblacioDAO(connexion).get(c.getInt(getColumnIndex(c,"codi"))));
        pl.setPlace_id(c.getString(getColumnIndex(c,"place_id")));
        pl.setLat(c.getDouble(getColumnIndex(c,"lat")));
        pl.setLon(c.getDouble(getColumnIndex(c,"lon")));
        pl.setVecindad(c.getString(getColumnIndex(c,"vecindad")));
        pl.setFotoReference(c.getString(getColumnIndex(c,"foto")));
        pl.setPhone(c.getString(getColumnIndex(c,"telefono")));
        return pl;
    }

    @Override
    public String getKey(Place obj) {
        return obj.getPlace_id();
    }
    // TODO REFACTOR THIS

    public List<Place> getFor(Poblacio pob,String type) {
        PoblacioDAO dao = new PoblacioDAO(connexion);
        List<Place> places = new ArrayList<>();
        if(dao.get(pob.getCodi()) != null && dao.timeToFetch(pob.getCodi(),type)) {
            //dao.insert(pob);
            FetchPlacesOf worker = new FetchPlacesOf(connexion);
            dao.actualizeFetchTime(pob.getCodi(),type);
            worker.addPoblacion(pob,type);
            worker.execute();
        }
        Cursor select = db.rawQuery("SELECT place_id FROM Sitios WHERE codi = "+pob.getCodi(),null);
        while (select.moveToNext()) {
            Place p = this.get(select.getString(0));
            if(p != null &&  Arrays.asList(p.getTipos()).contains(new Tipos(type))) {
                places.add(p);
            }
        }
        return places;
    }
    public List<Place> getFor(Poblacio pob,Tipos type) {
        return this.getFor(pob,type.getGoogle_type());
    }

    public FetchPlacesOf fetchFor(Poblacio pob, Tipos type) {
        PoblacioDAO dao = new PoblacioDAO(connexion);
        FetchPlacesOf worker = new FetchPlacesOf(connexion);
        dao.actualizeFetchTime(pob.getCodi(),type.getGoogle_type());
        worker.addPoblacion(pob,type.getGoogle_type());
        worker.execute();
        return worker;
    }
}
