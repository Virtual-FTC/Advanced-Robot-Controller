package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.basicwebsocket.Ros;
import com.qualcomm.robotcore.hardware.basicwebsocket.Topic;
import com.qualcomm.robotcore.hardware.basicwebsocket.messages.ftc.DcMotorInput;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class DcMotorPowerHandler {
    public static boolean canUpdatePower = false;
    private static HashMap<String, Double> powers = new HashMap<>();
    private static long currentTime;

    public static void setPower(String name, double power) {
        if (powers.containsKey(name)) {
            powers.remove(name);
        }
        powers.put(name, power);
    }

    public static void stopPowerThread() {
        canUpdatePower = false;
    }

    private static Ros client = null;

    private static Topic motorPub;

    String motorName;

    public static void startPowerThread() {
        try {
            client = new Ros(new URI("ws://" + DcMotorImpl.rosIp + ":9091"));
            client.connect();
        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
        motorPub = new Topic(client, "/unity//input", "ftc_msgs/DcMotorInput");
        DcMotorInput toSend = new DcMotorInput(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, new String[]{"", "", "", "", "", "", "", ""});
        motorPub.publish(toSend);

        currentTime = System.currentTimeMillis();


        canUpdatePower = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(canUpdatePower) {
                    if(System.currentTimeMillis() > currentTime + 10) {

                        currentTime = System.currentTimeMillis();
                    }
                }
            }
        });
        thread.setName("RobotPowerThread");
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    public void publishCmdVel(){
        //TODO: use config file to get names
        DcMotorInput dcMotorInputToSend = new DcMotorInput(new double[]{powers.get(""), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, new String[]{"", "", "", "", "", "", "", ""});
        motorPub.publish(dcMotorInputToSend);
    }
}
