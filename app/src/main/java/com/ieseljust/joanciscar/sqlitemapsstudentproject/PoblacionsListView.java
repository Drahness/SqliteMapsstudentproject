package com.ieseljust.joanciscar.sqlitemapsstudentproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ieseljust.joanciscar.sqlitemapsstudentproject.DAO.PoblacioDAO;
import com.ieseljust.joanciscar.sqlitemapsstudentproject.models.Poblacio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoblacionsListView extends MainMenu {
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<Poblacio> poblacioList = new PoblacioDAO(this).get();
        List<Map<String,Object>> adapterMap = new ArrayList<>();
        listView = findViewById(R.id.poblacions_list);
        for (Poblacio pob : poblacioList) {
            Map<String,Object> mapped = new HashMap<>();
            mapped.put("codi",pob.getCodi());
            mapped.put("name",pob.getNom());
            mapped.put("lat",pob.getLat());
            mapped.put("lon",pob.getLon());
            mapped.put("radius",pob.getRadius());
            adapterMap.add(mapped);
        }
        SimpleAdapter sa = new SimpleAdapter(this,
                adapterMap,
                R.layout.poblacion_item,
                new String[] {"codi","name","lat","lon","radius"},
                new int[] {R.id.codi_itemlist,
                        R.id.name_itemlist,
                        R.id.latitude_itemlist,
                        R.id.longitude_itemlist,
                        R.id.radius_itemlist});
        listView.setAdapter(sa);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("poblacio",poblacioList.get((int)id));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.content_poblacions_list_view;
    }
}