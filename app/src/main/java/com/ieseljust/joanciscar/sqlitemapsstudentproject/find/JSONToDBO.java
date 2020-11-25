package com.ieseljust.joanciscar.sqlitemapsstudentproject.find;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JSONToDBO {
    private enum Types {
        BOOLEAN(Boolean.class),STRING,FLOAT,INT,NULL,JSONOBJECT,JSONARRAY;
        private Class<?> klass;
        Types(Class<?> klass) {
            this.klass = klass;
        }
        Types() {}
    }
    private List<String> columns = new ArrayList<>();;
    private List<JSONToDBO> childs;
    private JSONObject json;
    public JSONToDBO(JSONObject o) throws JSONException {
        json = o;
        JSONArray names = o.names();
        for (int i = 0; i < Objects.requireNonNull(names).length(); i++) {
            columns.add(names.getString(i));
        }
    }
    public JSONToDBO(JSONArray objectToParse) throws JSONException {
        this(objectToParse.getJSONObject(0));
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @NonNull
    @Override
    public String toString() {
        try {
            return json.toString(4);
        } catch (JSONException e) {
            e.printStackTrace();
            return e.toString();
        }
    }
    public String toSQL() {
        return null;
    }
}
