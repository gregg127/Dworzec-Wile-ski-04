package com.example.grzesiek.oktorejodjazd;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewBusStopActivity extends AppCompatActivity {

    private final static String[] options = {"GENERAL", "SPECIFIC"};
    private final static String ZTMUrl = "http://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1";

    private TextView txt;
    private EditText input;
    private ProgressBar progress;
    private Button checkButton;
    private ListView generalBusStopList;
    private ListView specificBusStopList;
    private Map<String, String> map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_bus_lay);
        init();
    }

    private void init(){
        txt = (TextView)findViewById(R.id.busStopTextView);
        input = (EditText)findViewById(R.id.busStopInput);
        progress = (ProgressBar)findViewById(R.id.busStopProgressBar);
        progress.setVisibility(View.GONE);
        progress.getIndeterminateDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        checkButton = (Button)findViewById(R.id.busStopBtn);
        generalBusStopList = (ListView)findViewById(R.id.busStopSuggestions);
        specificBusStopList = (ListView)findViewById(R.id.busStopOutput);
        checkButton.setOnClickListener(e -> {
            String temp = transform(input.getText().toString());
            if(temp != null)
                new BusStopsSearchTask().execute(ZTMUrl, temp, options[0]);
            else
                toastMessage("Niepoprawna nazwa", false);
        });
        generalBusStopList.setOnItemClickListener((par, view, pos, id) -> {
            String url = null;
            String name = par.getItemAtPosition(pos).toString();
            for (Map.Entry<String, String> entry : map.entrySet()){
                if (entry.getValue().equals(name)) {
                    url = entry.getKey();
                    break;
                }
            }
            new BusStopsSearchTask().execute(
                    "http://www.ztm.waw.pl/"+url,
                    name.replaceAll(" -.*", ""),
                    options[1]
            );
        });
        specificBusStopList.setOnItemClickListener((par, view, pos, id) -> {
            String name = par.getItemAtPosition(pos).toString();
            String url = null;
            Database db = Database.getInstance(this);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getValue().equals(name)) {
                    url = entry.getKey();
                    break;
                }
            }
            url = "http://www.ztm.waw.pl/"+url;
            name = par.getItemAtPosition(pos).toString().replaceAll(" ».*","");
            if(!db.inDatabase(url)){
                db.addBusStop(name, url);
                toastMessage("Dodano do listy!", true);
            } else {
                toastMessage("Ten przystanek jest już obecny na Twojej liście", true);
            }
        });
    }

    private void toastMessage(String msg, boolean isLong){
        int temp;
        if(isLong)
            temp = Toast.LENGTH_LONG;
        else
            temp = Toast.LENGTH_SHORT;
        Toast.makeText(getApplicationContext(), msg, temp).show();
    }

    private void switchListsVisibility(boolean generalListVis){
        if(generalListVis){
            generalBusStopList.setVisibility(View.VISIBLE);
            specificBusStopList.setVisibility(View.GONE);
        } else {
            generalBusStopList.setVisibility(View.GONE);
            specificBusStopList.setVisibility(View.VISIBLE);
        }
    }

    private void progressBarIsEnabled(boolean bool){
        progress.setIndeterminate(bool);
        if(bool)
            progress.setVisibility(View.VISIBLE);
        else
            progress.setVisibility(View.GONE);
    }

    private String transform(String s){
        String[] busExceptions = {"3 maja", "bzów", "róż", "osiedle młodych", "mirków"};

        s = s.toLowerCase().trim().replaceAll("\\d*$", "").trim(); //trailing nums

        if(s.contains("dworzec") || s.contains("dw. "))
            s = s.replaceAll("(dworzec )|(dw\\. )", "dw.");
        if(s.contains("plac") || s.contains("pl. "))
            s = s.replaceAll("(plac )|(pl\\. )","pl.");
        if(s.contains("księdza") || s.contains("ksiedza") || s.contains("ks. "))
            s = s.replaceAll("(księdza )|(ksiedza )|(ks\\. )","ks.");
        if(s.contains("aleja") || s.contains("al. ") || s.contains("al.")){
            boolean editable = true;
            String ex = "";
            for(String e: busExceptions){
                if(s.contains(e)) {
                    editable = false;
                    ex = e;
                    break;
                }
            }
            if(editable)
                s = s.replaceAll("(al\\. )|(aleja )","al.");
            else {
                if(ex.equals("3 maja")){
                    s = s.replaceAll("(aleja )|(al\\.)","al. ");
                    s = s.replaceAll(" {2,}"," ");
                } else
                    s = s.replaceAll("(al\\. ?)","aleja ");
            }
        }
        if((s.contains("osiedle ") || s.contains("os. ") || s.contains("os."))
                && s.replaceFirst("^osiedle","").length() > 3){
            boolean editable = true;
            String ex = "";
            for(String e : busExceptions)
                if (s.contains(e)){
                    editable = false;
                    ex = e;
                    break;
                }

            if(editable)
                s = s.replaceFirst("(^osiedle )|(os\\. )","os.");
            else if(!ex.equals("osiedle młodych"))
                s = s.replaceAll("os\\. ?","osiedle ");
        }
        if(s.contains("ratusz")){
            s = s.replaceAll(" ", "-");
        }

        if(s.length() < 3)
            return null;
        s = s+" "; // space added to avoid substrings
        return s;
    }


    // may have garbage collector difficulties
    private class BusStopsSearchTask extends AsyncTask<String, Void, List<String>> {
        private String option;

        @Override
        protected void onPreExecute(){
            toastMessage("Szukam ...", false);
            progressBarIsEnabled(true);
        }

        @Override
        protected List<String> doInBackground(String... strings) {
            //string[0] - URL
            //string[1] - bus stop name
            //string[2] - option
            option = strings[2];
            if(option.equals("GENERAL")){
                map = RozkladJazdy.getGeneralBusStops(strings[0], strings[1]);
            } else if(option.equals("SPECIFIC")){
                map = RozkladJazdy.getSpecificBusStops(strings[0], strings[1]);
            }

            if(map == null)
                return null;
            return new ArrayList<>(map.values());
        }

        @Override
        protected  void onPostExecute(List<String> result){
            progressBarIsEnabled(false);
            if(result == null){ // exception
                generalBusStopList.setAdapter(null);
                toastMessage("Wystąpił błąd przy pobieraniu strony." +
                        " Upewnij się, że masz działający internet i spróbuj ponownie", true);

            } else if(result.size() == 0){ // nothing found
                generalBusStopList.setAdapter(null);
                toastMessage("Nie znaleziono takiej lokalizacji", true);

            } else if(option.equals("GENERAL")){
                switchListsVisibility(true);
                generalBusStopList.setAdapter( new ArrayAdapter<>(
                        NewBusStopActivity.this,
                        android.R.layout.simple_list_item_1,
                        result
                ));
                toastMessage("Kliknij na przystanek z odpowiednim miastem", false);

            } else if(option.equals("SPECIFIC")){
                switchListsVisibility(false);
                specificBusStopList.setAdapter( new ArrayAdapter<>(
                        NewBusStopActivity.this,
                        android.R.layout.simple_list_item_1,
                        result
                ));
                toastMessage("Kliknij na przystanek, który chcesz dodać do swojej listy",
                        false);
            }
        }
    }
}