package com.example.grzesiek.oktorejodjazd;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Grzesiek on 2017-05-16.
 */

public abstract class RozkladJazdy {
    public static String getRozkladJazdy(String source, int godz){
        Pattern p = Pattern.compile("\\s("+godz+":\\d{2})(.*?)\\(");
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
        if(sb.toString().length() == 0 ){
            return "O godzinie " +godz+ " nic nie odjeżdża z tego przystanku :(";
        }
        return sb.toString();
    }
    public static String getSourceCode(String url){
        String src = "";
        try {
            Document docTemp = Jsoup.connect(url).timeout(4000).get();
            src = docTemp.text();
        } catch (Exception ex){
            return ex.toString();
        }
        return src;
    }
}
