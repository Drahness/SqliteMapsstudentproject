package com.ieseljust.joanciscar.sqlitemapsstudentproject.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.ieseljust.joanciscar.sqlitemapsstudentproject.utils.Locales;

import java.io.Serializable;
import java.util.Objects;

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tipos tipos = (Tipos) o;
        return google_type.equals(tipos.google_type) &&
                Objects.equals(local_type, tipos.local_type);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(google_type, local_type);
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
