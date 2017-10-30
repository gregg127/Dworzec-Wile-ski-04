package com.example.grzesiek.oktorejodjazd;

class Przystanek {
    private final String url;
    private volatile String sourceCode;
    private volatile boolean ready;
    Przystanek(String url) {
        this.url = url;
    }

    void startDownloading() {
       new Thread(() -> {
           sourceCode = RozkladJazdy.getSourceCode(url);
           ready = true;
       }).start();
    }
    String getRozkladJazdy(String hour) {
        if (!ready) {
            return "Pobieram rozkład, poczekaj chwilę i spróbuj ponownie.";
        } else if (sourceCode.length() < 200) { // Exception case
            ready = false;
            startDownloading();
            if(sourceCode.contains("SocketTimeoutException")){
                return "Przekroczono dopuszczalny czas pobierania strony.\n" +
                        "Upewnij się, że masz działający internet, poczekaj chwilę " +
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
                    return RozkladJazdy.getRozkladJazdy(sourceCode, godzina);
                }
            } catch (NumberFormatException ex) {
                return "Nic nie zostało wpisane. \nWpisz godzinę.";
            }
        }
    }
}
