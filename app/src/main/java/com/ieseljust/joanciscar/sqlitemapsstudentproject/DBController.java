package com.ieseljust.joanciscar.sqlitemapsstudentproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.GenericDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.PlaceDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.PoblacioDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.TiposDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.beans.Place;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.beans.Poblacio;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.beans.Tipos;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.find.FetchPlacesOf;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DBController extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Lugares";
    private static final int DATABASE_VERSION = 1;
    private final Context context;

    public DBController(Context context) {
        super (context, DATABASE_NAME, null,  DATABASE_VERSION);
        this.context = context;
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("AAAAA");
        db.execSQL("CREATE TABLE Poblaciones (\n" +
                "codi INTEGER PRIMARY KEY,\n" +
                "nom VARCHAR NOT NULL,\n" +
                "lat REAL NOT NULL,\n" +
                "lon REAL NOT NULL,\n" +
                "radius INTEGER  NOT NULL)");
        db.execSQL("CREATE TABLE Tipos (\n" +
                "google_type VARCHAR PRIMARY KEY,\n" +
                "tipo VARCHAR )");
        db.execSQL("CREATE TABLE Sitios (\n" +
                "name VARCHAR NOT NULL,\n" +
                "place_id VARCHAR PRIMARY KEY,\n" +
                "codi INTEGER NOT NULL," +
                "lat REAL NOT NULL,\n" +
                "lon REAL NOT NULL,\n" +
                "vecindad VARCHAR NOT NULL,\n" +
                "foto BLOB," +
                "FOREIGN KEY (codi) REFERENCES Poblaciones(codi))");
        db.execSQL("CREATE TABLE Tipos_Sitios (\n" +
                "place_id VARCHAR,\n" +
                "tipo VARCHAR,\n" +
                "PRIMARY KEY(place_id, tipo))");
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

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

    }
}
