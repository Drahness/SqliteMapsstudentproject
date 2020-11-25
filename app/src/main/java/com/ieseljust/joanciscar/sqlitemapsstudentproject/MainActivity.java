package com.ieseljust.joanciscar.sqlitemapsstudentproject;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.find.JSONToDBO;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends MainMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView txt = findViewById(R.id.textView123);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject O = new JSONObject("{\n" +
                            "\"business_status\": \"OPERATIONAL\",\n" +
                            "\"geometry\": {\n" +
                            "\"location\": {\n" +
                            "\"lat\": 39.12111530000001,\n" +
                            "\"lng\": -0.4487013999999999\n" +
                            "},\n" +
                            "\"viewport\": {\n" +
                            "\"northeast\": {\n" +
                            "\"lat\": 39.1224325302915,\n" +
                            "\"lng\": -0.4473652197084979\n" +
                            "},\n" +
                            "\"southwest\": {\n" +
                            "\"lat\": 39.1197345697085,\n" +
                            "\"lng\": -0.450063180291502\n" +
                            "}\n" +
                            "}\n" +
                            "},\n" +
                            "\"icon\": \"https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/shopping-71.png\",\n" +
                            "\"name\": \"Druni Perfumerías ®\",\n" +
                            "\"opening_hours\": {\n" +
                            "\"open_now\": false\n" +
                            "},\n" +
                            "\"photos\": [\n" +
                            "{\n" +
                            "\"height\": 500,\n" +
                            "\"html_attributions\": [\n" +
                            "\"\\u003ca href=\\\"https://maps.google.com/maps/contrib/113752162977887521274\\\"\\u003eDruni Perfumerías ®\\u003c/a\\u003e\"\n" +
                            "],\n" +
                            "\"photo_reference\": \"ATtYBwKAL6V9a9RfhAyP_V26ImnmxkLb2mKxn0JnHTq40NtZTc2IcwvgLj50sIv8OQqD-K4_S5eBtOdhR1AKLyjgfpuoF_g5QChzw8yT9Lc5MZAV-JzF4WXGseY2rxg5DZBB-8FaoWPWnOB27Epd580DGaBYXNXIGLh-6Q6bKmjbdyysTVnM\",\n" +
                            "\"width\": 500\n" +
                            "}\n" +
                            "],\n" +
                            "\"place_id\": \"ChIJ-71Pk9K6YQ0R33mEzmAJ4LI\",\n" +
                            "\"plus_code\": {\n" +
                            "\"compound_code\": \"4HC2+CG Carcaixent, Spain\",\n" +
                            "\"global_code\": \"8CFX4HC2+CG\"\n" +
                            "},\n" +
                            "\"rating\": 4.3,\n" +
                            "\"reference\": \"ChIJ-71Pk9K6YQ0R33mEzmAJ4LI\",\n" +
                            "\"scope\": \"GOOGLE\",\n" +
                            "\"types\": [\n" +
                            "\"pharmacy\",\n" +
                            "\"gym\",\n" +
                            "\"health\",\n" +
                            "\"point_of_interest\",\n" +
                            "\"clothing_store\",\n" +
                            "\"store\",\n" +
                            "\"establishment\"\n" +
                            "],\n" +
                            "\"user_ratings_total\": 49,\n" +
                            "\"vicinity\": \"Carrer de Sant Francesc d'Assís, 7, Carcaixent\"\n" +
                            "}");

                    txt.setText(new JSONToDBO(O).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected int getActivityLayout() {return R.layout.content_main;}
}
