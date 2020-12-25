package com.qualcomm.robotcore.hardware;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class DcMotorMaster {

    /**
     * DcMotorImpl Objects
     */

    private static DcMotorImpl motorImpl1;
    private static DcMotorImpl motorImpl2;
    private static DcMotorImpl motorImpl3;
    private static DcMotorImpl motorImpl4;
    private static DcMotorImpl motorImpl5;
    private static DcMotorImpl motorImpl6;
    private static DcMotorImpl motorImpl7;
    private static DcMotorImpl motorImpl8;

    public static void setDcMotor1(DcMotorImpl dcMotor) {
        motorImpl1 = dcMotor;
    }

    public static void setDcMotor2(DcMotorImpl dcMotor) {
        motorImpl2 = dcMotor;
    }

    public static void setDcMotor3(DcMotorImpl dcMotor) {
        motorImpl3 = dcMotor;
    }

    public static void setDcMotor4(DcMotorImpl dcMotor) {
        motorImpl4 = dcMotor;
    }

    public static void setDcMotor5(DcMotorImpl dcMotor) {
        motorImpl5 = dcMotor;
    }

    public static void setDcMotor6(DcMotorImpl dcMotor) {
        motorImpl6 = dcMotor;
    }

    public static void setDcMotor7(DcMotorImpl dcMotor) {
        motorImpl7 = dcMotor;
    }

    public static void setDcMotor8(DcMotorImpl dcMotor) {
        motorImpl8 = dcMotor;
    }

    /**
     * UnityRTC Client Connection / Thread Handler
     */

    public static String ip = "";

    public static void start() {
        //Connect to server as client

        currentTime = System.currentTimeMillis();

        //start thread to send messages to server

        startMotorInputThread();
    }

    public static void disconnect() {
        //stop thread

        canRunMotorInputThread = false;

        //disconnect client
    }


    /**
     * Motor Input Functions
     */

    private static long currentTime;
    private static boolean canRunMotorInputThread = false;

    private static void startMotorInputThread() {
        canRunMotorInputThread = true;
        Thread motorInputThread = new Thread(new Runnable() {
            @Override
            public void run() {
                currentTime = System.currentTimeMillis();
                while (canRunMotorInputThread) {
                    // send motor input every 15 milliseconds
                    if (System.currentTimeMillis() >= currentTime + 15) {
                        currentTime = System.currentTimeMillis();
                        publishMotorJSONCommand();
                    }
                }
            }
        });
        motorInputThread.setName("MotorInputThread");
        motorInputThread.setPriority(Thread.MAX_PRIORITY);
        motorInputThread.start();
    }

    private static void publishMotorJSONCommand() {

    }
}
