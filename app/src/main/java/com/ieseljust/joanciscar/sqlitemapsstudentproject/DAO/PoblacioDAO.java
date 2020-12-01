package com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Tipos;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.utils.DBController;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Poblacio;

import java.util.ArrayList;
import java.util.List;

public class PoblacioDAO implements GenericDAO<Poblacio,Integer> {
    public SQLiteOpenHelper connexion;
    public SQLiteDatabase db;
    private static SQLiteStatement poblationsInsertStatement;

    public PoblacioDAO(SQLiteOpenHelper sqLiteOpenHelper) {
        this.connexion = sqLiteOpenHelper;
        this.db = connexion.getWritableDatabase();
        getCompiledInsert(db);
    }
    private PoblacioDAO(){}

    @Override
    public List<Poblacio> get() {
        Cursor selectAll = db.rawQuery("SELECT * FROM Poblaciones",null);
        List<Poblacio> poblacions = new ArrayList<>();
        while (selectAll.moveToNext()) {
            poblacions.add(getFromCursor(selectAll));
        }
        return poblacions;
    }

    @Override
    public boolean insert(Poblacio obj) {
        SQLiteStatement stat = getCompiledInsert(null);
        this.putInStatement(obj,stat,null);
        return stat.executeInsert() != -1;
    }

    @Override
    public Integer getKey(Poblacio obj) {
        return obj.getCodi();
    }

    @Override
    public boolean update(Poblacio obj, Integer key) {
        SQLiteStatement st = db.compileStatement("UPDATE Poblaciones SET codi = ?" +
                ", nom = ?" +
                ", lat = ?" +
                ", lon = ?" +
                ", radius = ?" +
                " WHERE codi = ?");
        this.putInStatement(obj,st,null);
        st.bindLong(6,key);
        return st.executeUpdateDelete() > 0;
    }

    @Override
    public boolean delete(Integer key) {
        SQLiteStatement st = db.compileStatement("DELETE FROM Poblaciones " +
                " WHERE codi = ?");
        st.bindLong(1,key);
        return st.executeUpdateDelete() > 0;
    }

    @Override
    public Poblacio get(Integer key) {
        Cursor cursor = db.rawQuery(String.format("SELECT * from Poblaciones where codi = %s",key),null);
        Poblacio p = null;
        if(cursor.moveToNext()) {
            p = getFromCursor(cursor);
        }
        cursor.close();
        return p;

    }

    @Override
    public List<Poblacio> getWhere(String where) {
        Cursor selectAll = db.rawQuery("SELECT * FROM Poblaciones "+where,null);
        List<Poblacio> poblacions = new ArrayList<>();
        while (selectAll.moveToNext()) {
            poblacions.add(getFromCursor(selectAll));
        }
        return poblacions;
    }

    /**
     *
     * @param obj instance to put in.
     * @param stat the statement to put the stats of the obj
     * @param startIndex null == 1
     */
    public void putInStatement(Poblacio obj, SQLiteStatement stat, @Nullable Integer startIndex) {
        if(startIndex == null) {
            startIndex = 1;
        }
        stat.bindLong(startIndex,obj.getCodi());
        stat.bindString(startIndex+1,obj.getNom());
        stat.bindDouble(startIndex+2,obj.getLat());
        stat.bindDouble(startIndex+3,obj.getLon());
        stat.bindLong(startIndex+4,obj.getRadius());
    }

    public Poblacio getFromCursor(Cursor c) {
        Poblacio p = new Poblacio();
        p.setCodi(c.getInt(getColumnIndex(c,"codi")));
        p.setNom(c.getString(getColumnIndex(c,"nom")));
        p.setLat(c.getDouble(getColumnIndex(c,"lat")));
        p.setLon(c.getDouble(getColumnIndex(c,"lon")));
        p.setRadius(c.getInt(getColumnIndex(c,"radius")));
        return p;
    }

    public static SQLiteStatement getCompiledInsert(@Nullable SQLiteDatabase db) {
        if(poblationsInsertStatement == null) {
            poblationsInsertStatement = db.compileStatement("INSERT INTO Poblaciones VALUES (?,?,?,?,?)");
        }
        return poblationsInsertStatement;
    }

    public boolean timeToFetch(Integer codi, String type) {
        Cursor select = db.rawQuery("SELECT time FROM LAST_FETCH_POBLACIONES WHERE codi = ? and google_type = ?",new String[] {codi.toString(),type});
        if(select.moveToNext()) {
            return select.getLong(0) > (3600 * 24);
        }
        return true;

    }

    public void actualizeFetchTime(Integer codi, String type) {
        SQLiteStatement deleteStatement = db.compileStatement("DELETE FROM LAST_FETCH_POBLACIONES WHERE codi = ? and google_type = ?");
        deleteStatement.bindString(2,type);
        deleteStatement.bindLong(1,codi);
        deleteStatement.executeUpdateDelete();
        SQLiteStatement insertStatementdb = db.compileStatement("INSERT INTO LAST_FETCH_POBLACIONES (codi,time,google_type) VALUES (?,?,?)");
        insertStatementdb.bindLong(2,System.currentTimeMillis()/1000);
        insertStatementdb.bindLong(1,codi);
        insertStatementdb.bindString(3,type);
        deleteStatement.executeUpdateDelete();
        insertStatementdb.executeInsert();
    }
}
