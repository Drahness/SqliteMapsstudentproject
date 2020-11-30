package com.ieseljust.joanciscar.sqlitemapsstudentproject.utils;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Locales {
    private static Locales instance;
    private final Map<String,String> locale = new HashMap<>();
    private final Resources resources;
    private final String pack;

    private Locales(Context context) {
        resources = context.getResources();
        pack = context.getPackageName();
        instance = this;
    }

    public static Locales getInstance(@NonNull Context context) {
        if(instance == null) {
            instance = new Locales(context);
        }
        return instance;
    }

    public String getLocal(String googleType) {
        if(!locale.containsKey(googleType)) {
            int identifier = resources.getIdentifier(googleType,"string",pack);
            if(identifier == 0) {
                return null;
                //throw new Resources.NotFoundException("In the strings xml, needs a translated string for "+googleType+".");
            }
            String string = resources.getString(identifier);
            locale.put(googleType,string);
            return string;
        }
        return locale.get(googleType);
    }

    /**
     * You need to init the class with a default context.
     * @param googleType the type in the array of Types
     * @return the translation of the type.
     */
    public static String getStaticLocal(String googleType) {
        if(instance == null) {
            throw new RuntimeException("You need to init the class via the getInstance method with a context.");
        }
        return instance.getLocal(googleType);
    }
}
