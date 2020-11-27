package com.ieseljust.joanciscar.sqlitemapsstudentproject.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBController extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Lugares";
    private static final int DATABASE_VERSION = 3;
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
                "foto VARCHAR," +
                "FOREIGN KEY (codi) REFERENCES Poblaciones(codi))");
        db.execSQL("CREATE TABLE Tipos_Sitios (\n" +
                "place_id VARCHAR,\n" +
                "google_type VARCHAR,\n" +
                "FOREIGN KEY (place_id) REFERENCES Sitios(place_id)," +
                "FOREIGN KEY (google_type) REFERENCES TIpos(google_type))");
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
        if(oldVersion == 1 && newVersion > oldVersion) {
            db.execSQL("PRAGMA foreign_keys=off;");
            db.execSQL("DROP TABLE Sitios;");
            db.execSQL("CREATE TABLE Sitios (\n" +
                    "name VARCHAR NOT NULL,\n" +
                    "place_id VARCHAR PRIMARY KEY,\n" +
                    "codi INTEGER NOT NULL," +
                    "lat REAL NOT NULL,\n" +
                    "lon REAL NOT NULL,\n" +
                    "vecindad VARCHAR NOT NULL,\n" +
                    "foto VARCHAR," +
                    "FOREIGN KEY (codi) REFERENCES Poblaciones(codi))");
            db.execSQL("PRAGMA foreign_keys=on;");
            oldVersion++;
        }// version 2
        if(oldVersion == 2 && newVersion > oldVersion) {
            db.execSQL("PRAGMA foreign_keys=off;");
            db.execSQL("CREATE TABLE Tipos_Sitios2 (\n" +
                    "place_id VARCHAR,\n" +
                    "google_type VARCHAR,\n" +
                    "FOREIGN KEY (place_id) REFERENCES Sitios(place_id)," +
                    "FOREIGN KEY (google_type) REFERENCES TIpos(google_type))");
            db.execSQL("INSERT INTO Tipos_Sitios2 " +
                            "SELECT place_id,tipo " +
                            "FROM Tipos_Sitios");
            db.execSQL("DROP TABLE Tipos_Sitios");
            db.execSQL("ALTER TABLE Tipos_Sitios2 RENAME TO Tipos_Sitios");
            db.execSQL("PRAGMA foreign_keys=on;");
        }
        System.out.println(oldVersion+" level existing db to a level -> "+newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

    }

    private void modifyTable(SQLiteDatabase db, String tablename, String statement) {
        db.execSQL("PRAGMA foreign_keys=off;");
        db.execSQL("CREATE TABLE "+tablename+"2 (\n" +
                "place_id VARCHAR,\n" +
                "google_type VARCHAR,\n" +
                "FOREIGN KEY (place_id) REFERENCES Sitios(place_id)," +
                "FOREIGN KEY (google_type) REFERENCES TIpos(google_type))");
        db.execSQL("INSERT INTO  " +tablename + "2" +
                "SELECT place_id,tipo " +
                "FROM Tipos_Sitios");
        db.execSQL("DROP TABLE "+tablename);
        db.execSQL("ALTER TABLE "+tablename+"2 RENAME TO "+tablename);
        db.execSQL("PRAGMA foreign_keys=on;");
    }
}
/*
SQLiteDatabase db = dbHelper.getReadableDatabase();
String table = "table_name"; Ejemplo query
String[] columnsToReturn = { "column_1", "column_2" };
String selection = "column_1 =?";
String[] selectionArgs = { someValue }; // matched to "?" in selection
Cursor dbCursor = db.query(table, columnsToReturn, selection, selectionArgs, null, null, null);
 */