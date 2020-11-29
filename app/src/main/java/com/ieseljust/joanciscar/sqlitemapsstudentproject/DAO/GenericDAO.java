package com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

public interface GenericDAO<T, K> {

    List<T> get();

    boolean insert(T obj);

    boolean update(T obj, K key);

    boolean delete(K key);

    @Nullable
    T get(K key);

    List<T> getWhere(String where);

    /**
     * Basic method to put a obj in a statement, dont use this method in complex statements, only put in
     * insert and update simple
     * @param obj the object to add to the statement
     * @param stat
     * @param integer
     */
    public void putInStatement(T obj, SQLiteStatement stat, @Nullable Integer integer);

    /**
     * Retrieves a basic instance with a SELECT * FROM X without any relationship, DONT CALL THIS IF
     * NOT IS A BASIC TYPE OR YOU WILL FACE NULLPOINTERS... If you dont fix the relations.
     * Ej: If you have an entity with a relation with another one to many, that many cant be fetched.
     *     Only is fetched if you call the method {@link GenericDAO#get()} or {@link GenericDAO#get()}
     * @param c          the cursor in a valid position
     * @return
     */
    T getFromCursor(Cursor c);

    default int getColumnIndex(Cursor c ,String label) {
        return Arrays.asList(c.getColumnNames()).indexOf(label);
    }

}
