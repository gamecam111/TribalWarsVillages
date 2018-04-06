package eu.gamecam.tribalwarsvillages;

/**
 * Created by Erik Juríček on 4.4.2018.
 */
public class Logs {
    private String time;
    private boolean result;

    public Logs(String time, boolean result) {
        this.time = time;
        this.result = result;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
