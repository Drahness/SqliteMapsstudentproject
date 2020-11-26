package com.ieseljust.joanciscar.sqlitemapsstudentproject.find;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.google.android.gms.tasks.Task;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.DBController;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.MainMenu;

public class Tasker extends AsyncTaskLoader<Boolean> {
    public Tasker(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    public Boolean loadInBackground() {
        MainMenu.instance = new DBController(this.getContext());
        return true;
    }

}
