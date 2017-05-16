package com.example.grzesiek.dworzecwilenski04;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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

    private String sourceCode;
    private volatile boolean ready;

    private CheckedTextView output;
    private Button button;
    private EditText mEdit;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT) // RozkladJazdy.getSourceCode()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratusz_arsenal_lay);

        new Thread(new Runnable(){
            @Override
            public void run() {
                sourceCode = RozkladJazdy.getSourceCode(site); //sets the HTML code to source variable
                ready = true;
            }
        }).start();

        mEdit = (EditText)findViewById(R.id.timeTextFieldRatuszArsenal);
        output = (CheckedTextView)findViewById(R.id.outputRatuszArsenal);
        output.setMovementMethod(new ScrollingMovementMethod());
        button = (Button) findViewById(R.id.checkScheduleRatuszArsenal);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(!ready){
                    output.setText("Pobieram rozkład, poczekaj chwilę i spróbuj ponownie.");
                }
                else if(sourceCode.length() < 100){ // in RozkladJazdy.getSourceCode() exception thrown case
                    output.setText(sourceCode);
                    ready = false;
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            sourceCode = RozkladJazdy.getSourceCode(site); //sets the HTML code to source variable
                            ready = true;
                        }
                    }).start();
                } else {
                    int godzina = 0;
                    try{
                        godzina = Integer.parseInt(mEdit.getText().toString());
                        if(godzina < 0 || godzina > 23){
                            output.setText("Podano złą godzinę");
                        } else
                            output.setText(RozkladJazdy.getRozkladJazdy(sourceCode,godzina));
                    } catch (NumberFormatException ex) {
                        output.setText("Nic nie zostało wpisane. \nPoproszę o podanie godziny.");
                    }

                }
            }
        });
    }
}