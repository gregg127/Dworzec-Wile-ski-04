package com.example.grzesiek.dworzecwilenski04;

/**
 * Created by Grzesiek on 2017-06-20.
 */

public class Przystanek {
    private final String url;
    private String sourceCode;
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
        } else if (sourceCode.length() < 100) { // ???
            ready = false;
            startDownloading();
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
                return "Nic nie zostało wpisane. \nPoproszę o podanie godziny.";
            }
        }
    }
}
