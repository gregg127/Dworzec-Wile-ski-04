package com.example.grzesiek.oktorejodjazd;

import java.net.SocketTimeoutException;

/**
 * Created by Grzesiek on 2017-06-20.
 */

public class Przystanek {
    private final String url;
    private volatile String sourceCode;
    private volatile boolean ready;
    public Przystanek(String url) {
        this.url = url;
    }

    public void startDownloading() {
       new Thread(new Runnable() {
           @Override
           public void run() {
               sourceCode = RozkladJazdy.getSourceCode(url);
               ready = true;
           }
       }).start();
    }
    public String getRozkladJazdy(String hour) {
        if (!ready) {
            return "Pobieram rozkład, poczekaj chwilę i spróbuj ponownie.";
        } else if (sourceCode.length() < 200) { // Exception case
            ready = false;
            startDownloading();
            if(sourceCode.contains("SocketTimeoutException")){
                return "Przekroczono dopuszczalny czas pobierania strony.\n" +
                        "Upewnij się, że masz działający internet " +
                        "i spróbuj ponownie.\n("+sourceCode+")";
            }
            return "Wystąpił błąd przy ładowaniu strony.\nWłącz internet i spróbuj ponownie.\n("
                    +sourceCode+")";
        } else {
            try {
                int godzina = Integer.parseInt(hour);
                if (godzina < 0 || godzina > 23) {
                    return "Podano złą godzinę";
                } else {
                    String temp = RozkladJazdy.getRozkladJazdy(sourceCode, godzina);
                    if(temp.length() == 0)
                        return "O godzinie "+godzina+" nic nie odjeżdża z tego przystanku :(";
                    return temp;
                }
            } catch (NumberFormatException ex) {
                return "Nic nie zostało wpisane. \nWpisz godzinę.";
            }
        }
    }
}
