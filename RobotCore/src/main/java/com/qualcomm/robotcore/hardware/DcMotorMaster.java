package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.basicwebsocket.RosUDP;
import com.qualcomm.robotcore.hardware.basicwebsocket.UdpTopic;
import com.qualcomm.robotcore.hardware.basicwebsocket.callback.TopicCallback;
import com.qualcomm.robotcore.hardware.basicwebsocket.messages.Message;
import com.qualcomm.robotcore.hardware.basicwebsocket.messages.ftc.DcMotorInput;
import com.qualcomm.robotcore.hardware.basicwebsocket.messages.ftc.MotorInputs;

import javax.json.JsonObject;

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
     * ROS Client Connection / Thread Starter
     */

    private static DcMotorInput motor1;
    private static DcMotorInput motor2;
    private static DcMotorInput motor3;
    private static DcMotorInput motor4;
    private static DcMotorInput motor5;
    private static DcMotorInput motor6;
    private static DcMotorInput motor7;
    private static DcMotorInput motor8;

    public static String rosIp = "35.232.174.143";
    public static RosUDP client = null;
    private static UdpTopic motorPub;
    private static UdpTopic motorOutputPub;


    public static void start() {
        client = new RosUDP(rosIp, 9092);
        client.connect();

        motorPub = new UdpTopic(client, "/unity/motors/input", "ftc_msgs/MotorInputs");

        currentTime = System.currentTimeMillis();


        motorOutputPub = new UdpTopic(client, "/unity/motors/output", "ftc_msgs/MotorOutputs");
        motorOutputPub.subscribe(new TopicCallback() {
            @Override
            public void handleMessage(Message message) {
                JsonObject data = message.toJsonObject();
                if (data.getJsonNumber("encoder_data").doubleValue() != 0) {
                    motorImpl1.actualPosition = data.getJsonNumber("encoder_data").doubleValue();
                    motorImpl1.encoderPosition = motorImpl1.direction == DcMotorSimple.Direction.REVERSE ? (motorImpl1.encoderBasePosition - motorImpl1.actualPosition) : -(motorImpl1.encoderBasePosition - motorImpl1.actualPosition);
                }
            }
        });

        startMotorInputThread();
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
                        motor1 = new DcMotorInput(motorImpl1.power, "");
                        motor2 = new DcMotorInput(motorImpl2.power, "");
                        motor3 = new DcMotorInput(motorImpl3.power, "");
                        motor4 = new DcMotorInput(motorImpl4.power, "");
                        motor5 = new DcMotorInput(motorImpl5.power, "");
                        motor6 = new DcMotorInput(motorImpl6.power, "");
                        motor7 = new DcMotorInput(motorImpl7.power, "");
                        motor8 = new DcMotorInput(motorImpl8.power, "");
                        publishCmd();
                    }
                }
            }
        });
        motorInputThread.setName("MotorInputThread");
        motorInputThread.setPriority(Thread.MAX_PRIORITY);
        motorInputThread.start();
    }

    private static void publishCmd() {
        MotorInputs motorInputs = new MotorInputs(motor1, motor2, motor3, motor4, motor5, motor6, motor7, motor8);
        motorPub.publish(motorInputs);
    }
}
