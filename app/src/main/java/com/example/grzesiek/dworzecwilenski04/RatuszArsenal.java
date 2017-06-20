package com.example.grzesiek.dworzecwilenski04;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;

/**
 * Created by Grzesiek on 2017-05-10.
 */

public class RatuszArsenal extends AppCompatActivity {
    final private String site = "http://www.ztm.waw.pl/rozklad_nowy.php?c=182&l=1&n=7099&o=09";
    private Przystanek przystanek;
    private CheckedTextView output;
    private Button button;
    private EditText mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratusz_arsenal_lay);
        przystanek = new Przystanek(site);
        przystanek.startDownloading();
        addComponents();
    }
    private void addComponents(){
        mEdit = (EditText)findViewById(R.id.timeTextFieldRatuszArsenal);
        output = (CheckedTextView)findViewById(R.id.outputRatuszArsenal);
        output.setMovementMethod(new ScrollingMovementMethod());
        button = (Button) findViewById(R.id.checkScheduleRatuszArsenal);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                output.setText(przystanek.getRozkladJazdy(mEdit.getText().toString()));
            }
        });
    }
}