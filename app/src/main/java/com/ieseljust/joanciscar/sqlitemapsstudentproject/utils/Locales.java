package com.ieseljust.joanciscar.sqlitemapsstudentproject.utils;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Locales {
    private static Locales instance;
    private final Map<String,String> locale = new HashMap<>();
    private final Resources resources;
    private final String pack;

    public Locales(Context context) {
        resources = context.getResources();
        pack = context.getPackageName();
    }

    public static Locales getInstance(@Nullable Context context) {
        if(context == null && instance == null) {
            throw new RuntimeException("Called getInstance without parameters");
        }
        else if(context != null && instance == null) {
            instance = new Locales(context);
        }
        return instance;
    }

    private String getLocal(String googleType) {
        if(!locale.containsKey(googleType)) {
            int identifier = resources.getIdentifier(googleType,null,pack);
            String string = resources.getString(identifier);
            locale.put(googleType,string);
            return string;
        }
        return locale.get(googleType);
    }

    public static String getLocale(String googleType) {
        return Locales.getInstance(null).getLocal(googleType);
    }
}
