package com.ieseljust.joanciscar.sqlitemapsstudentproject;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DBController extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Poblacions";
    private static final int DATABASE_VERSION = 1;

    public DBController(Context context) {
        super (context, DATABASE_NAME, null,  DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Poblacion (" +
                "INT codi PRIMARY KEY," +
                "VARCHAR(30) nom NO NULL" +
                "DOUBLE lat NO NULL," +
                "DOUBLE lon NO NULL," +
                "INT radius NO NULL)");
        db.execSQL("INSERT INTO Poblacion VALUES (46712,'Piles',38.9408685,-0.1324241,1000)");
        db.execSQL("INSERT INTO Poblacion VALUES (46711,'Miramar',38.9501877,-0.1405837,1000)");
        db.execSQL("INSERT INTO Poblacion VALUES (46713,'Bellreguard',38.9466531,-0.1624822,2000)");
        db.execSQL("INSERT INTO Poblacion VALUES (46715,'Alqueria de la comtessa',38.9367938,-0.1509766,1000)");
        db.execSQL("INSERT INTO Poblacion VALUES (46702,'Gandia',38.96735,-0.1853385,5000)");
        db.execSQL("INSERT INTO Poblacion VALUES (46701,'Grao Gandia',39.007931,-0.1679302,5000)");
        db.execSQL("INSERT INTO Poblacion VALUES (46740,'Carcaixent',39.1214619,-0.4479085,2000)");
        db.execSQL("CREATE TABLE Tipos (" +
                "VARCHAR(20) googletype PRIMARY KEY," +
                "VARCHAR(20) tipo NO NULL)");
        db.execSQL("CREATE TABLE Sitio (" +
                "INT codi NOT NULL" +
                "VARCHAR(35) place_id PRIMARY KEY," +
                "DOUBLE lat NO NULL," +
                "DOUBLE lon NO NULL," +
                "VARCHAR(80) calle NO NULL," +
                "VARCHAR(20) tipo NO NULL" +
                "BLOB foto)");
        Cursor cursor = db.rawQuery("SELECT codi,lat,lon FROM Poblacion;",null);

        cursor.



    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
