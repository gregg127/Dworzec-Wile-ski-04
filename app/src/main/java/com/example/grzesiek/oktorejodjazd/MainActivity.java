package com.example.grzesiek.oktorejodjazd;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity{

    private Database db;
    private ArrayList<String> busStopsNames;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private TextView emptyListTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onResume(){
        super.onResume();
        refresh();
    }

    private void init(){
        db = Database.getInstance(this);
        getDatabaseData();
        initializeTextView();
        initializeListViewCom();
        initFloatingActionButton();
    }

    private void getDatabaseData(){
        busStopsNames = db.getBusStopsNames();
        Collections.sort(busStopsNames, String.CASE_INSENSITIVE_ORDER);
    }

    private void initializeTextView(){
        emptyListTextView = (TextView) findViewById(R.id.noBusStopsTextView);
        if(busStopsNames.size() != 0){
            setEmptyListMessageVisible(false);
        }
    }

    private void initializeListViewCom(){
        listView = (ListView)findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, busStopsNames);
        listView.setAdapter(adapter);
        listView.setLongClickable(true);
        listView.setOnItemClickListener((par,view,pos,id) -> {
            Intent intent = new Intent(MainActivity.this , PrzystanekActivity.class);
            intent.putExtra("site",db.getURL(busStopsNames.get(pos)));
            startActivity(intent);
        });
        listView.setOnItemLongClickListener((adapterView, view, pos, id) -> {
            dialog(pos);
            return true;
        });
    }

    private void initFloatingActionButton(){
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NewBusStopActivity.class)));
    }

    private void dialog(int pos){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setMessage(R.string.deleteDialogMessage)
                .setTitle(R.string.deleteDialogTitle)
                .setIcon(android.R.drawable.ic_delete)
                .setPositiveButton(R.string.positiveDialogBtn, (dial, id) -> {
                    if(db.delete(listView.getItemAtPosition(pos).toString())){
                        adapter.remove(listView.getItemAtPosition(pos).toString());
                        refresh();
                    }
                })
                .setNegativeButton(R.string.negativeDialogBtn, (dial, id) -> {
                    // cancel
                })
                .show();
    }

    private void setEmptyListMessageVisible(boolean isEmpty){
        if(isEmpty)
            emptyListTextView.setVisibility(View.VISIBLE);
        else
            emptyListTextView.setVisibility(View.GONE);
    }
    private void refresh(){
        adapter.clear();
        getDatabaseData();
        if(busStopsNames.size() == 0){
            setEmptyListMessageVisible(true);
        } else {
            setEmptyListMessageVisible(false);
            adapter.addAll(busStopsNames);
            adapter.notifyDataSetChanged();
        }
    }
}