package com.example.grzesiek.oktorejodjazd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;

/**
 * Created by Grzesiek on 2017-10-20.
 */

public class PrzystanekActivity extends AppCompatActivity {
    private String site ;
    private Przystanek przystanek;
    private CheckedTextView output;
    private Button button;
    private EditText mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.przystanek_lay);
        addComponents();
        przystanek.startDownloading();
    }

    private void addComponents(){
        Intent intent = getIntent();
        site = intent.getStringExtra("site");
        przystanek = new Przystanek(site);
        mEdit = (EditText)findViewById(R.id.timeTextField);
        output = (CheckedTextView)findViewById(R.id.output);
        output.setMovementMethod(new ScrollingMovementMethod());
        button = (Button) findViewById(R.id.checkSchedule);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                output.setText(przystanek.getRozkladJazdy(mEdit.getText().toString()));
            }
        });
    }
}
