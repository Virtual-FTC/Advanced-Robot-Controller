package org.firstinspires.ftc.robotcore.external;

public interface Telemetry {

    //This is public, static, and final, by default
    StringBuilder data = new StringBuilder(200);

    /**
     * Add data for display by telemetry
     * @param caption
     * @param fmt   Standard Java format string for display of data
     * @param data  list of data items for display
     */
    public void addData(String caption, String fmt, Object... data);

    /**
     * Add data for display by telemetry
     * @param caption
     * @param data  data object to display
     */
    public void addData(String caption, Object data);

    /**
     * Clear the telemetry display, then write any data that has been added since the previous update.
     */
    public void update();
}
