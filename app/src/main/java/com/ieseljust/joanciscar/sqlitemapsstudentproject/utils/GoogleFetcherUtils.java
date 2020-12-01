package com.ieseljust.joanciscar.sqlitemapsstudentproject.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ieseljust.joanciscar.sqlitemapsstudentproject.MainMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public final class GoogleFetcherUtils {
    public static String API_KEY = MainMenu.API_KEY;

    public static JSONObject getJSONContent(URL url) throws IOException, JSONException {
        System.out.println(url.toString());
        URLConnection connection = url.openConnection();
        InputStream is = connection.getInputStream();
        BufferedReader br = new BufferedReader( new InputStreamReader(is));
        String c;
        StringBuilder sb = new StringBuilder();
        while((c = br.readLine()) != null) {
            sb.append(c);
        }
        return new JSONObject(sb.toString());
    }

    public static Bitmap getImage(URL url) throws IOException {
        Bitmap mIcon11 = null;
        InputStream in = url.openStream();
        mIcon11 = BitmapFactory.decodeStream(in);
        return mIcon11;
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    public static Bitmap getBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    public static byte[] getImageBytes(URL url) throws IOException {
        return getBytes(getImage(url));
    }
    public static URL getImageURL(String imgReference,Integer max_witdh) throws MalformedURLException {
        StringBuilder sb = new StringBuilder();
        sb.append("https://maps.googleapis.com/maps/api/place/photo?");
        sb.append("maxwidth=").append(max_witdh).append('&');
        sb.append("photoreference=").append(imgReference).append('&');
        sb.append("key=").append(API_KEY);
        // https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=YOUR_API_KEY
        return new URL(sb.toString());
    }

    public URL getPlacesURL(String type, double lat, double lon, int radius) throws MalformedURLException {
        StringBuilder sb = new StringBuilder();
        sb.append("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
                .append("location=").append(lat).append(',').append(lon).append('&')
                .append("sensor=").append(true).append('&')
                .append("radius=").append(radius).append('&')
                .append("types=").append(type).append('&')
                .append("key=").append(API_KEY);
        return new URL(sb.toString());
//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=37.160317,-2.748416&sensor=true&radius=5000&types=pharmacy,market&key=AIzaSyB9GCrd7kBdpXeFq5QMI--dPYfNwaByWHc
    }

    public static URL getPlaceDetailsURL(String place_id,String...fields) throws MalformedURLException {
        //https://maps.googleapis.com/maps/api/place/details/json?place_id=ChIJN1t_tDeuEmsRUsoyG83frY4&fields=name,rating,formatted_phone_number&key=YOUR_API_KEY
        StringBuilder sb = new StringBuilder();
        sb.append("https://maps.googleapis.com/maps/api/place/details/json?").append("place_id=").append(place_id);
        sb.append("&fields=");
        for (String field:
             fields) {
            sb.append(field+",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("&key=").append(API_KEY);
        return new URL(sb.toString());
    }

    public static JSONObject getResults(URL url) throws JSONException, IOException {
        return getJSONContent(url).getJSONObject("result");
    }
}
