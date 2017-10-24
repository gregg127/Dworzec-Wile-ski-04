package com.example.grzesiek.oktorejodjazd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends  AppCompatActivity{

    Database db;

    HashMap<String, String> nameUrlMap;
    ArrayList<String> busStopNames;
    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        db = new Database(this);
        prepareMap();
        copyNamesToList();
        initializeListViewCom();
    }

    private void prepareMap(){

        db.addBusStop("Handlowa 03",
                "http://www.ztm.waw.pl/rozklad_nowy.php?c=182&l=1&n=1139&o=03",
                "hendlowa 03 src");
        db.addBusStop("Dworzec Wileński 04",
                "http://www.ztm.waw.pl/rozklad_nowy.php?c=182&l=1&n=1003&o=04",
                "wilkiss me assc");



        System.out.println(db.getResource("Dworzec Wileński 04", "url"));
        System.out.println(db.getResource("Handlowa 03", "ztm_source_code"));

        nameUrlMap = new HashMap<>();
        nameUrlMap.put("Handlowa 03",
                "http://www.ztm.waw.pl/rozklad_nowy.php?c=182&l=1&n=1139&o=03");
        nameUrlMap.put("Wilenski 04",
                "http://www.ztm.waw.pl/rozklad_nowy.php?c=182&l=1&n=1003&o=04");
        nameUrlMap.put("Wilenski 03",
                "http://www.ztm.waw.pl/rozklad_nowy.php?c=182&l=1&n=1003&o=03");
        nameUrlMap.put("Ratusz Arsenal 09",
                "http://www.ztm.waw.pl/rozklad_nowy.php?c=182&l=1&n=7099&o=09");

    }

    private void copyNamesToList(){
        busStopNames = new ArrayList<>();
        for(Map.Entry<String,String> e : nameUrlMap.entrySet())
            busStopNames.add(e.getKey());
    }

    private void initializeListViewCom(){
        listView = (ListView)findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, busStopNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this , PrzystanekActivity.class);
                intent.putExtra("site",(nameUrlMap.get(busStopNames.get(position))));
                startActivity(intent);
            }
        });
    }

    private void addBusStop(String name, String url){
        nameUrlMap.put(name, url);
        busStopNames.add(name);
        Collections.sort(busStopNames, String.CASE_INSENSITIVE_ORDER);
    }

    private String getBusStopUrl(String name){
        return null;
    }
}
