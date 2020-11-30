package com.ieseljust.joanciscar.sqlitemapsstudentproject.models;

import com.ieseljust.joanciscar.sqlitemapsstudentproject.utils.Locales;

import java.io.Serializable;

public class Tipos implements Serializable {
    private String google_type;
    private String local_type;

    public Tipos(String google_type, String local_type) {
        this.google_type = local_type;
        this.local_type = local_type;
    }

    @Override
    public String toString() {
        if(local_type == null) {
            return "--"+google_type+"--";
        }
        return local_type;

    }

    public Tipos(String google_type) {
        this.google_type = google_type;
        this.local_type = Locales.getStaticLocal(google_type);
    }
    public Tipos() {}

    public String getGoogle_type() {
        return google_type;
    }

    public void setGoogle_type(String google_type) {
        this.google_type = google_type;
    }

    public String getLocal_type() {
        return local_type;
    }

    public void setLocal_type(String local_type) {
        this.local_type = local_type;
    }

}
