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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Grzesiek on 2017-05-10.
 */

public class RatuszArsenal extends AppCompatActivity {
    final String site = "http://www.ztm.waw.pl/rozklad_nowy.php?c=182&l=1&n=7099&o=09";

    private boolean czyWystapilBlad;
    private boolean ready;
    private String source;
    private CheckedTextView output;
    private Button button;
    private EditText mEdit;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratusz_arsenal_lay);

        new Thread(new Runnable(){
            @Override
            public void run() {
                getSourceCode(); //sets the HTML code to source variable
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
                else if(czyWystapilBlad){ // in getSourceCode() exception thrown case
                    output.setText(source);
                    czyWystapilBlad = false;
                    getSourceCode();
                } else {
                    String godzina = mEdit.getText().toString();
                    output.setText(getRozkladJazdy(godzina));
                }
            }
        });
    }
    private String getRozkladJazdy(String godz){
        //================= PERMISSION PROBLEMS
        //http://stackoverflow.com/questions/17360924/securityexception-permission-denied-missing-internet-permission
        //================

        String regex = "\\s("+godz+":\\d{2})(.*?)\\(";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(source);
        ArrayList<String> list = new ArrayList<>();
        while(m.find()){
            list.add(m.group(1) + "   ||" + m.group(2));
        }
        StringBuilder sb = new StringBuilder();
        for(String e : list){
            sb.append(e);
            sb.append("\n");
        }
        return sb.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getSourceCode(){ // === WYKORZYSTANIE JSOUP
        try {
            Document docTemp = Jsoup.connect(site).get();
            source = docTemp.text();
        } catch(Exception ex){
            source = "Wystąpił błąd przy ładowaniu strony.\nWłącz internet i spróbuj ponownie.";
            czyWystapilBlad = true;
        }
    }
}