package com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import com.ieseljust.joanciscar.sqlitemapsstudentproject.DBController;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.beans.Tipos;

import java.util.ArrayList;
import java.util.List;

public class TiposDAO implements GenericDAO<Tipos,String>{
    private final SQLiteDatabase db;
    private static SQLiteStatement placeInsertStatement;
    private final DBController connexion;

    public TiposDAO(Context context) {
        this.connexion = new DBController(context);
        this.db = connexion.getWritableDatabase();
    }

    public static SQLiteStatement getCompiledInsert(@Nullable SQLiteDatabase db) {
        if(placeInsertStatement == null) {
            placeInsertStatement = db.compileStatement("INSERT INTO Tipos VALUES (?,?)");
        }
        return placeInsertStatement;
    }

    @Override
    public List<Tipos> get() {
        Cursor selectAll = db.rawQuery("SELECT * FROM Tipos",null);
        List<Tipos> tipos = new ArrayList<>();
        while (selectAll.moveToNext()) {
            tipos.add(getFromCursor(selectAll));
        }
        return tipos;
    }

    @Override
    public boolean insert(Tipos obj) {
        return false;// TODO
    }

    @Override
    public boolean update(Tipos obj, String key) {
        SQLiteStatement stat = db.compileStatement("UPDATE Tipos SET google_type = ?, tipo = ?" +
                "  WHERE google_type = ?");
        stat.bindString(1,key);
        return stat.executeUpdateDelete() > 0;
    }

    @Override
    public boolean delete(String key) {
        SQLiteStatement stat = db.compileStatement("DELETE FROM Tipos WHERE google_type = ?");
        stat.bindString(1,key);
        return stat.executeUpdateDelete() > 0;
    }

    @Override
    public Tipos get(String key) {
        Cursor selectAll = db.rawQuery("SELECT * FROM Tipos t where google_type" + key,null);
        Tipos tipos = null;
        if(selectAll.moveToNext()) {
            tipos = getFromCursor(selectAll);
        }
        return tipos;
    }

    @Override
    public List<Tipos> getWhere(String where) {
        Cursor selectAll = db.rawQuery("SELECT * FROM Tipos t" + where,null);
        List<Tipos> tipos = new ArrayList<>();
        while(selectAll.moveToNext()) {
            tipos.add(getFromCursor(selectAll));
        }
        return tipos;
    }

    @Override
    public void putInStatement(Tipos obj, SQLiteStatement stat, @Nullable Integer integer) {
        if(integer == null) {
            integer = 1;
        }
        stat.bindString(integer,obj.getGoogle_type());
        stat.bindString(integer+1 ,obj.getLocal_type());
    }


    @Override
    public Tipos getFromCursor(Cursor c) {
        Tipos t = new Tipos(c.getString(this.getColumnIndex(c,"google_type")));
        t.setLocal_type(c.getString(this.getColumnIndex(c,"tipo")));
        return t;
    }

}