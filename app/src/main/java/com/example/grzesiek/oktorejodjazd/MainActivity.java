package com.example.grzesiek.oktorejodjazd;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity{

    private Database db;
    private ArrayList<String> busStopsNames;
    private ArrayAdapter<String> adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onResume(){
        super.onResume();
        adapter.clear();
        getDatabaseData();
        adapter.addAll(busStopsNames);
        adapter.notifyDataSetChanged();
    }

    private void init(){
        db = Database.getInstance(this);
        getDatabaseData();
        initializeListViewCom();
        initFloatingActionButton();
    }

    private void getDatabaseData(){
        busStopsNames = db.getBusStopsNames();
        Collections.sort(busStopsNames, String.CASE_INSENSITIVE_ORDER);
    }

    private void initializeListViewCom(){
        listView = (ListView)findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, busStopsNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((par,view,pos,id) -> {
            Intent intent = new Intent(MainActivity.this , PrzystanekActivity.class);
            intent.putExtra("site",db.getURL(busStopsNames.get(pos)));
            startActivity(intent);
        });
    }

    private void initFloatingActionButton(){
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NewBusStopActivity.class)));
    }
}
