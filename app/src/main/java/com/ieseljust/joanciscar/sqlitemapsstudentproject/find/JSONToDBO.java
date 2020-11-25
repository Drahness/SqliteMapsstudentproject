package com.ieseljust.joanciscar.sqlitemapsstudentproject.find;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JSONToDBO {
    public static final String BOOLEAN = "BOOLEAN";
    public static final String INTEGER = "INTEGER";
    public static final String VARCHAR = "VARCHAR";
    public static final String DOUBLE = "DOUBLE";
    public static final String FLOAT = "DOUBLE";
    public static final String JSONARRAY = "COMPOSITE";
    public static final String JSONOBJECT = "TABLE_IDENTIFIER";

    public static String[] priority = {JSONOBJECT, JSONARRAY, VARCHAR, DOUBLE, FLOAT, INTEGER, BOOLEAN};
    private final String tablename;
    private final List<String> columnNames = new ArrayList<>();
    private final List<String> types = new ArrayList<>();
    private final List<JSONToDBO> childs = new ArrayList<>();
    private int columns;
    private JSONObject json;

    public JSONToDBO(JSONObject o, String tableName, @Nullable String relationID) throws JSONException {
        this.tablename = tableName;
        if(relationID != null) {
            columnNames.add(relationID);
            types.add("INTEGER");
        }
        json = o;
        JSONArray names = o.names();
        for (int i = 0; i < Objects.requireNonNull(names).length(); i++) {
            String name = names.getString(i);
            String type = getType(o.get(name));
            if (type.equals("COMPOSITE")) {
                childs.add(new JSONToDBO(o.getJSONArray(name), name, tableName + "_id"));
            } else if (type.equals("TABLE_IDENTIFIER")) {
                childs.add(new JSONToDBO(o.getJSONObject(name), name,"id"));
                name += "_id";
            }
            columnNames.add(name);
            types.add(type);
        }
        columns = columnNames.size();
    }

    private JSONToDBO(JSONArray jsonArray, String tableName, @Nullable String parent_id) throws JSONException {
        String type = null;
        this.tablename = tableName;
        for (int i = 0; i < jsonArray.length(); i++) {
            type = getTypeArray(jsonArray);
        }
        switch (type) {
            case BOOLEAN:
                columns = 2;
                columnNames.add("value");
                types.add( BOOLEAN);
                break;
            case INTEGER:
                columns = 2;
                columnNames.add("value");
                types.add( INTEGER);
                break;
            case DOUBLE:
                columns = 2;
                columnNames.add("value");
                types.add(DOUBLE);
                break;
            case VARCHAR:
                columns = 2;
                columnNames.add("value");
                types.add( VARCHAR);
                break;
            case JSONOBJECT:
                columnNames.add("value");
                types.add( JSONOBJECT);
                columns = 2;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONToDBO json = new JSONToDBO(jsonArray.getJSONObject(i), parent_id, null);
                    childs.add(json);
                }
                break;
            case JSONARRAY:
                throw new RuntimeException("Unsuported " + type);
        }
        columnNames.add(0, parent_id);
        types.add(0, type);
    }

    public JSONToDBO(JSONArray jsonArray, String tableName) throws JSONException {
        this(jsonArray, tableName, null);
    }

    private String getType(Object o) {
        if (o instanceof String) {
            return VARCHAR;
        } else if (o instanceof Integer) {
            return INTEGER;
        } else if (o instanceof Boolean) {
            return BOOLEAN;
        } else if (o instanceof Float) {
            return FLOAT;
        } else if (o instanceof Double) {
            return DOUBLE;
        } else if (o instanceof JSONArray) {
            return JSONARRAY; // Another table WITH THE ID OF THIS as a field
        } else if (o instanceof JSONObject) {
            return JSONOBJECT; // Link to another table
        }
        throw new RuntimeException(String.format("Unexpected type %s", o.getClass().getCanonicalName()));
    }

    private String getTypeArray(JSONArray jsonArray) throws JSONException {
        int lastType = priority.length - 1;
        for (int i = 0; i < jsonArray.length(); i++) {
            String currentType = getType(jsonArray.get(i));
            for (int j = 0; j < lastType; j++) {
                if (priority[i].equals(currentType)) {
                    lastType = i;
                    continue;
                }
            }
        }
        return priority[lastType];
    }

    private void merge(JSONToDBO another) throws JSONException {
        for (int i = 0; i < columns; i++) {
            if (!this.columnNames.contains(another.columnNames.get(i))) {
                this.columnNames.add(another.columnNames.get(i));
                this.types.add(another.types.get(i));
            }
        }
        for (int i = 0; i < this.childs.size(); i++) {
            this.merge(this.childs.get(i));
        }
    }
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
        String sql = String.format("CREATE TABLE %s (", tablename);
        for (int i = 0; i < columns; i++) {
            String type =  types.get(i);
            if(type.equals(JSONOBJECT)) {
                type = INTEGER;
            }
            if(type.equals(JSONARRAY)) {
                continue;
            }
            if (i == 0) {
                sql += String.format("%s %s ", type, columnNames.get(i));
            } else {
                sql += String.format(", %s %s ", type, columnNames.get(i));
            }
        }
        sql += " );";
        for (JSONToDBO json : childs) {
            sql += "\n\n" + json.toSQL();
        }
        return sql;
    }

    public String toSQLChild() {
        return null;
    }

    public String getTypeOrChild(int i) {
        String type = this.types.get(i);
        if(type.equals(JSONOBJECT)) {
            int y;
            for(y = 0 ; this.types.indexOf(JSONOBJECT) != i ; i++) {}
            return this.childs.get(y).types.get(0);
        }
        if(type.equals(JSONARRAY)) {
            int y;
            for(y = 0 ; this.types.indexOf(JSONARRAY) != i ; i++)
            return this.childs.get(y).types.get(0);
        }
        return null;
    }
}
