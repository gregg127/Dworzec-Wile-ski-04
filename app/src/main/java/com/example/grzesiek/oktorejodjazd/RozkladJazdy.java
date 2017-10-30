package com.example.grzesiek.oktorejodjazd;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class RozkladJazdy {
    static String getRozkladJazdy(String source, int godz){
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
    static String getSourceCode(String url){
        String src;
        try {
            Document docTemp = Jsoup.connect(url).timeout(5000).get();
            src = docTemp.text();
        } catch (Exception ex){
            return ex.toString();
        }
        return src;
    }

    static Map<String, String> getGeneralBusStops(String url, String busStop){
        Map<String, String> busURLsAndNames= new HashMap<>();
        try {
            Document temp = Jsoup.connect(url).get();
            Elements hrefs = temp.select("a[href][title]");
            for (Element el : hrefs){
                if(el.toString().toLowerCase().contains(busStop) && el.toString().contains("amp;a=")){
                    busURLsAndNames.put(
                            el.attr("href"),
                            el.attr("title"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return busURLsAndNames;
    }

    static Map<String, String> getSpecificBusStops(String url, String busStop){
        Map<String, String> busURLsAndNames= new HashMap<>();
        List<String> urls = new ArrayList<>();
        List<String> names = new ArrayList<>();
        try {
            Document temp = Jsoup.connect(url).timeout(8000).get();
            Elements hrefs = temp.select("a");
            Elements ps = temp.select("p");
            for (Element el : hrefs)
                if(el.toString().contains(busStop))
                    urls.add(el.attr("href"));

            for(Element el : ps)
                if(el.toString().contains(busStop) && el.toString().contains("przystanek"))
                   names.add(el.text().replace("przystanek: ", ""));

            for(int i=0; i<urls.size(); i++)
                busURLsAndNames.put(urls.get(i), names.get(i));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return busURLsAndNames;
    }
}
