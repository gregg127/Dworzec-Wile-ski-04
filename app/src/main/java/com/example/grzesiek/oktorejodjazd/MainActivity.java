package com.example.grzesiek.oktorejodjazd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToHandlowa03(View view){
        startActivity( new Intent(this, Handlowa03.class) );
    }

    public void goToWilenski04(View view){
        startActivity( new Intent(this, Wilenski04.class) );
    }

    public void goToWilenski03(View view){
        startActivity( new Intent(this, Wilenski03.class));
    }

    public void goToRatuszArsenal09(View view){
        startActivity( new Intent(this, RatuszArsenal09.class) );
    }

}
