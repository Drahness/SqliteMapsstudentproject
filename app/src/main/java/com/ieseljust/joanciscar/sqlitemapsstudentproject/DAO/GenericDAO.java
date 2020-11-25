package com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO;

import android.database.sqlite.SQLiteDatabase;

import com.ieseljust.joanciscar.sqlitemapsstudentproject.DBController;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.MainActivity;

import java.util.List;

public interface GenericDAO<T, K> {
    public List<T> get();
    public boolean insert();
    public boolean update();
    public boolean delete();
    public T get(K key);
}
