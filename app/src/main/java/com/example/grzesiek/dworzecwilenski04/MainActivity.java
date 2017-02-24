package com.example.grzesiek.dworzecwilenski04;

import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {
    final String site = "http://www.ztm.waw.pl/rozklad_nowy.php?c=182&l=1&n=1003&o=04";

    boolean czyWystapilBlad;
    String source;
    CheckedTextView output;
    Button button;
    EditText mEdit;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Two very important lines
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getSourceCode(); //sets the HTML code to source variable

        mEdit = (EditText)findViewById(R.id.editText);
        output = (CheckedTextView)findViewById(R.id.checkedTextView);
        output.setMovementMethod(new ScrollingMovementMethod());
        button = (Button) findViewById(R.id.buttonOne);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(czyWystapilBlad){ // w przypadku wyrzucenia wyjatku przy getSourceCode()
                    output.setText(source);
                    czyWystapilBlad = false;
                    getSourceCode();
                }else {
                    String godzina = mEdit.getText().toString();
                    output.setText(getRozkladJazdy(godzina));
                }
            }
        });
    }
    private String getRozkladJazdy(String godz){

        //================= PERMISSION PROBLEMS look down below
        //http://stackoverflow.com/questions/17360924/securityexception-permission-denied-missing-internet-permission
        //================

        //String regex = "<div class=\"wwgodz\">(" + godz + ":\\d{1,2}).*?<strong>(\\d{1,3}).*?\\d{2}\">(.*?)<span";
        String regex = "\\s("+godz+":\\d{2})(.*?)\\(";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(source);
        ArrayList<String> list = new ArrayList<>();
        while(m.find()){
            //list.add(m.group(1) + " || " + m.group(2) + " >> " + m.group(3));
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
        try{
            // === BAD CHOICE
            /*connection = new URL("http://www.ztm.waw.pl/rozklad_nowy.php?c=182&l=1&n=1139&o=03").openConnection();
            Scanner sc = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8.name());
            sc.useDelimiter("\\Z");
            String tempHtml = sc.next();
            sc.close();*/

            // === GOOD CHOICE
            Document docTemp = Jsoup.connect(site).get();
            source = docTemp.text();
        } catch(Exception ex){ // DODAJ OBSLUGE BLEDU W PRZYPADKU BRAKU INTERNETU
            source = "Wystąpił błąd przy ładowaniu strony.\nWłącz internet i spróbuj ponownie.";
            czyWystapilBlad = true;
        }
    }
}
