package com.qualcomm.robotcore.hardware;

/**
 * Implementation of the GyroSensor interface
 */
public class GyroSensorImpl implements GyroSensor {
    private boolean initialized = false;
    private double initialHeading = 0.0;
    private double heading = 0.0;

    private long latencyNanos = 0;
    private long prevNanos = System.nanoTime();

    /**
     * For internal use only.
     * @param bot
     */

    /**
     * Initialize the gyro sensor
     */
    public synchronized void init(){
        initialized = true;
        heading = initialHeading;
    }

    /**
     * For internal use only.
     */
    public synchronized void deinit(){
        initialized = false;
        initialHeading = 0.0;
        heading = 0.0;
    }

    /**
     * Get heading in degrees (-180 to 180)
     * @return heading in degrees (-180 to 180)
     */
    public synchronized double getHeading(){
        if (initialized){
            double result = heading - initialHeading;
            if (result < -180.0) result += 360.0;
            else if (result > 180.0) result -= 360.0;
            return result;
        }
        else return 0.0;
    }

    /**
     * For internal use only.
     * @param heading
     */
    public synchronized void updateHeading(double heading){
        long nanos = System.nanoTime();
        if (nanos < (prevNanos + latencyNanos)) return;
        this.heading = heading;
        prevNanos = nanos;
    }
}
