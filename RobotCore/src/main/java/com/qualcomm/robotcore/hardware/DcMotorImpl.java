package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.configuration.MotorType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
//import org.json.JSONObject;

import java.io.File;
import java.io.StringReader;
import java.util.Random;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Implementation of the DcMotor interface.
 */
public class DcMotorImpl implements DcMotor {
    public final MotorType MOTOR_TYPE;
    public final MotorConfigurationType MOTOR_CONFIGURATION_TYPE;

    //Proportionate coefficient for RUN_TO_POSITION mode
    private final double COEFF_PROPORTIONATE = 5.0;
    //Target position for RUN_TO_POSITION mode
    private int targetPosition = 0;

    private final Random random = new Random();
    private RunMode mode = RunMode.RUN_WITHOUT_ENCODER;
    protected Direction direction = Direction.FORWARD;

    //power is the requested speed, normalized to the -1 to +1 range
    public double power = 0.0;

    //speed is the actual speed, normalized to the -1 to +1 range
    private double speed = 0.0;

    //actual position of motor
    public double actualPosition = 0.0;

    //position to use as baseline for encoder tick calculation
    public double encoderBasePosition = 0.0;

    private double randomErrorFrac = 0.0;
    private double systematicErrorFrac = 0.0;
    private double inertia;

    private ZeroPowerBehavior zeroPowerBehavior = ZeroPowerBehavior.BRAKE;

    public double encoderPosition;

    /**
     * For internal use only.
     *
     * @param motorType
     */

    public DcMotorImpl(String motorType) {
        MOTOR_TYPE = MotorType.Neverest20;
        MOTOR_CONFIGURATION_TYPE = new MotorConfigurationType(MOTOR_TYPE);
        checkMotorNumberAndUpdateMaster(motorType);
    }

    public static File filesDir;

    public static String activeConfigContent = "";

    private void checkMotorNumberAndUpdateMaster(String name) {
        try {
            JsonReader jsonReader = Json.createReader(new StringReader(activeConfigContent));
            JsonObject object = jsonReader.readObject();
            jsonReader.close();


            for (int i = 0; i < object.getJsonArray("devices").size(); i++) {
                if (name.equals(object.getJsonArray("devices").get(i).toString().replace("\"", ""))) {
                    if(i == 0) {
                        DcMotorMaster.setDcMotor1(this);
                    } else if(i == 1) {
                        DcMotorMaster.setDcMotor2(this);
                    } else if(i == 2) {
                        DcMotorMaster.setDcMotor3(this);
                    } else if(i == 3) {
                        DcMotorMaster.setDcMotor4(this);
                    } else if(i == 4) {
                        DcMotorMaster.setDcMotor5(this);
                    } else if(i == 5) {
                        DcMotorMaster.setDcMotor6(this);
                    } else if(i == 6) {
                        DcMotorMaster.setDcMotor7(this);
                    } else if(i == 7) {
                        DcMotorMaster.setDcMotor8(this);
                        DcMotorMaster.start();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Set mode of operation
     *
     * @param mode
     */
    public synchronized void setMode(RunMode mode) {
        this.mode = mode;
        power = 0.0;
        if (mode == RunMode.STOP_AND_RESET_ENCODER) {
            encoderBasePosition = encoderPosition;
        }
    }

    /**
     * Get mode of operation
     *
     * @return mode
     */
    public synchronized RunMode getMode() {
        return mode;
    }

    /**
     * Set logical direction
     *
     * @param direction the direction to set for this motor
     */
    public synchronized void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Get logical direction
     *
     * @return direction
     */
    public synchronized Direction getDirection() {
        return direction;
    }

    /**
     * Get requested power
     *
     * @return
     */
    public synchronized double getPower() {
        return power;
    }

    /**
     * Set requested power
     *
     * @param power the new power level of the motor, a value in the interval [-1.0, 1.0]
     */

    public synchronized void setPower(double power) {
        this.power = direction == Direction.REVERSE ? -power : power;
    }


    /**
     * Get actual speed
     *
     * @return actual speed, in ticks per sec
     */
    protected synchronized double getSpeed() {
        return speed;
    }

    /**
     * Get current position (as number of encoder ticks)
     *
     * @return number of encoder ticks
     */
    public synchronized int getCurrentPosition() {
        return (int) (encoderPosition - encoderBasePosition);
    }

    /**
     * For internal use only.
     *
     * @return
     */
    public synchronized double getActualPosition() {
        return actualPosition;
    }

    /**
     * For internal use only.
     * Updates motor speed based on current speed, power, and inertia. Then, uses motor speed to update position.
     *
     * @param milliseconds number of milliseconds since last update
     * @return change in actualPosition
     */
    public synchronized double update(double milliseconds) {
        if (mode == RunMode.STOP_AND_RESET_ENCODER) return 0.0;
        else if (mode == RunMode.RUN_TO_POSITION) {
            double targetSpeed = COEFF_PROPORTIONATE * (double) (targetPosition - getCurrentPosition())
                    / MOTOR_TYPE.MAX_TICKS_PER_SECOND;
            double absPower = Math.abs(power);
            targetSpeed = Math.max(-absPower, Math.min(targetSpeed, absPower));
            speed = speed + (1.0 - inertia) * (targetSpeed - speed);
        } else {
            speed = speed + (1.0 - inertia) * (power - speed);
        }
        double positionChange = speed * MOTOR_TYPE.MAX_TICKS_PER_SECOND * milliseconds / 1000.0;
        positionChange *= (1.0 + systematicErrorFrac + randomErrorFrac * random.nextGaussian());
        if (direction == Direction.FORWARD && MOTOR_TYPE.REVERSED ||
                direction == Direction.REVERSE && !MOTOR_TYPE.REVERSED)
            positionChange = -positionChange;
        actualPosition += positionChange;
        return positionChange;
    }

    /**
     * For internal use only.
     *
     * @param rdmErrFrac
     */
    public synchronized void setRandomErrorFrac(double rdmErrFrac) {
        randomErrorFrac = rdmErrFrac;
    }

    /**
     * For internal use only.
     *
     * @param sysErrFrac
     */
    public synchronized void setSystematicErrorFrac(double sysErrFrac) {
        systematicErrorFrac = sysErrFrac;
    }

    /**
     * For internal use only.
     *
     * @param in
     */
    public synchronized void setInertia(double in) {
        if (in < 0) inertia = 0.0;
        else if (in > 0.99) inertia = 0.99;
        else inertia = in;
    }

    //For internal use only: for stopping and resetting motor between op mode runs
    public synchronized void stopAndReset() {
        power = 0.0;
        speed = 0.0;
        actualPosition = 0.0;
        encoderBasePosition = 0.0;
        direction = Direction.FORWARD;
    }

    //Set target position for RUN_TO_POSITION mode
    public synchronized void setTargetPosition(int pos) {
        targetPosition = pos;
    }

    //Get target position
    public synchronized int getTargetPosition() {
        return targetPosition;
    }

    /**
     * Indicates whether approaching target in RUN_TO_POSITION mode
     * Result will become false when: ticks are nearly at the target AND speed is very slow
     */
    public synchronized boolean isBusy() {
        final double MAX_ROT_OFFSET = 0.02;
        int pos = getCurrentPosition();
        boolean atTarget = Math.abs(pos - targetPosition) / MOTOR_TYPE.TICKS_PER_ROTATION < MAX_ROT_OFFSET;
        boolean almostStopped = Math.abs(speed) / (COEFF_PROPORTIONATE * MOTOR_TYPE.TICKS_PER_ROTATION) < MAX_ROT_OFFSET;
        return mode == RunMode.RUN_TO_POSITION && Math.abs(power) > 0.0001 && (!atTarget || !almostStopped);
    }

    public synchronized void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        this.zeroPowerBehavior = zeroPowerBehavior;
    }

    public synchronized ZeroPowerBehavior getZeroPowerBehavior() {
        return this.zeroPowerBehavior;
    }

    public MotorConfigurationType getMotorType() {
        return MOTOR_CONFIGURATION_TYPE;
    }
}
