package com.example.grzesiek.dworzecwilenski04;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button wilenski = (Button) findViewById(R.id.wilenskiButton);
        Button handlowa = (Button) findViewById(R.id.handlowaButton);

        //Internet access thing
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void goToWilenski(View view){
        startActivity( new Intent(this, Wilenski.class) );
    }

    public void goToHandlowa(View view){
        startActivity( new Intent(this, Handlowa.class) );
    }

    public void goToRatuszArsenal (View view){
        startActivity(new Intent(this, RatuszArsenal.class));
    }
}
