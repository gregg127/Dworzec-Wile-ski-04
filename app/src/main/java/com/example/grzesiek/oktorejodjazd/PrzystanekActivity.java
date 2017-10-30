package com.example.grzesiek.oktorejodjazd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;

public class PrzystanekActivity extends AppCompatActivity {
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
        przystanek = new Przystanek( getIntent().getStringExtra("site"));
        mEdit = (EditText)findViewById(R.id.timeTextField);
        output = (CheckedTextView)findViewById(R.id.output);
        output.setMovementMethod(new ScrollingMovementMethod());
        button = (Button) findViewById(R.id.checkSchedule);
        button.setOnClickListener( v -> {
            output.setText(przystanek.getRozkladJazdy(mEdit.getText().toString()));
        });
    }
}
