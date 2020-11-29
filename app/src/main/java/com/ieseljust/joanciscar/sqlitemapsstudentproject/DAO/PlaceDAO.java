package com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;

import androidx.annotation.Nullable;

import com.ieseljust.joanciscar.sqlitemapsstudentproject.find.FetchPlacesOf;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Poblacio;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.utils.DBController;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Place;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Tipos;

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
        TiposDAO tiposDAO = new TiposDAO(context);
        for (Tipos tipo: obj.getTipos()) {
            tipos.bindString(1,obj.getPlace_id());
            tipos.bindString(2,tipo.getGoogle_type());
            if(tiposDAO.get(tipo.getGoogle_type()) != null) {
                tiposDAO.insert(tipo);
            };
            i+=tipos.executeInsert();
        }
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
                "foto = ?" +
                "where place_id = ?");
        SQLiteStatement updates = db.compileStatement("UPDATE Tipos_Sitios SET place_id = ?, google_type = ? WHERE place_id = ?");
        TiposDAO tiposDAO = new TiposDAO(context);
        for (int i = 0; i < obj.getTipos().length; i++) {
            updates.bindString(1,obj.getPlace_id());
            updates.bindString(2,obj.getTipos()[i].getGoogle_type());
            if(tiposDAO.get(obj.getTipos()[i].getGoogle_type()) != null) {
                tiposDAO.insert(obj.getTipos()[i]);
            };
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
            Cursor selecttipos = db.rawQuery("SELECT * FROM Tipos_Sitios WHERE place_id = "+"'"+selectAll.getString(3)+"'",null);
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
            Cursor selecttipos = db.rawQuery("SELECT * FROM TIPOS WHERE place_id = "+"'"+selectAll.getString(3)+"'",null);
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
        pl.setFotoReference(c.getString(getColumnIndex(c,"foto")));
        return pl;
    }

    public List<Place> getFor(Poblacio pob,String type) {
        PoblacioDAO dao = new PoblacioDAO(context);
        List<Place> places = new ArrayList<>();
        if(dao.get(pob.getCodi()) != null) {
            //dao.insert(pob);
            FetchPlacesOf worker = new FetchPlacesOf(context);
            worker.addPoblacion(pob,type);
            worker.execute();
            //while(worker.getStatus() != AsyncTask.Status.FINISHED);
        }
        Cursor select = db.rawQuery("SELECT * FROM Sitios WHERE codi = "+pob.getCodi(),null);
        while (select.moveToNext()) {
            places.add(getFromCursor(select));
        }
        return places;
    }
}
