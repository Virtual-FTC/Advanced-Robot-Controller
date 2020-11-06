package com.qualcomm.robotcore.hardware;

public class Telemetry {
    boolean isAutoClearing = false;
    public void setAutoClear(boolean state) {
        isAutoClearing = state;
    }

    String telemetryText = "";

    public void addData(String caption, Object... data) {
        if(isAutoClearing) telemetryText = "";
        telemetryText += "\n" + caption + ": " + data.toString();
    }

    /**
     * Add data for display by telemetry
     * @param caption
     * @param data  data object to display
     */
    public void addData(String caption, Object data) {
        if(isAutoClearing) telemetryText = "";
        telemetryText += "\n" + caption + ": " + data.toString();
    }

    /**
     * Clear the telemetry display, then write any data that has been added since the previous update.
     */
    public void update() {
        //TODO: add implementation for telemetry on textView
    }
}
