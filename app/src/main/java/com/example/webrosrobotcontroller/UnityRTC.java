package com.example.webrosrobotcontroller;

import com.qualcomm.robotcore.hardware.DcMotorMaster;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UnityRTC {
    
    /**
     * UnityRTC Client Connection / Thread Handler
     */

    public static String ip = "";
    private static UnityPeerConnection unityPeerConnection;


    public static void connect() {
        //Connect to server as client

        unityPeerConnection = new UnityPeerConnection();

        unityPeerConnection.connect();

        currentTime = System.currentTimeMillis();

        //start thread to send messages to server

        startMotorInputThread();
    }

    public static void disconnect() {
        //stop thread

        canRunMotorInputThread = false;

        //disconnect client
        unityPeerConnection.disconnect();
    }


    /**
     * Motor Input Functions
     */

    private static long currentTime;
    private static boolean canRunMotorInputThread = false;

    private static void startMotorInputThread() {
        canRunMotorInputThread = true;
        Thread motorInputThread = new Thread(() -> {
            currentTime = System.currentTimeMillis();
            while (canRunMotorInputThread) {
                // send motor input every 15 milliseconds
                if(unityPeerConnection.getMessage() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(unityPeerConnection.getMessage());
                        DcMotorMaster.motorImpl1.encoderPosition = (double) jsonObject.getJSONArray("motorPowers").get(0);
                        DcMotorMaster.motorImpl2.encoderPosition = (double) jsonObject.getJSONArray("motorPowers").get(1);
                        DcMotorMaster.motorImpl3.encoderPosition = (double) jsonObject.getJSONArray("motorPowers").get(2);
                        DcMotorMaster.motorImpl4.encoderPosition = (double) jsonObject.getJSONArray("motorPowers").get(3);
                        DcMotorMaster.motorImpl5.encoderPosition = (double) jsonObject.getJSONArray("motorPowers").get(4);
                        DcMotorMaster.motorImpl6.encoderPosition = (double) jsonObject.getJSONArray("motorPowers").get(5);
                        DcMotorMaster.motorImpl7.encoderPosition = (double) jsonObject.getJSONArray("motorPowers").get(6);
                        DcMotorMaster.motorImpl8.encoderPosition = (double) jsonObject.getJSONArray("motorPowers").get(7);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (System.currentTimeMillis() >= currentTime + 15) {
                    currentTime = System.currentTimeMillis();
                    publishMotorJSONCommand();
                }
            }
        });
        motorInputThread.setName("MotorInputThread");
        motorInputThread.setPriority(Thread.MAX_PRIORITY);
        motorInputThread.start();
    }

    private static void publishMotorJSONCommand() {
        try {
            //create motor power json object : {"motorPowers" : [1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0]}
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(DcMotorMaster.motorImpl1.power);
            jsonArray.put(DcMotorMaster.motorImpl2.power);
            jsonArray.put(DcMotorMaster.motorImpl3.power);
            jsonArray.put(DcMotorMaster.motorImpl4.power);
            jsonArray.put(DcMotorMaster.motorImpl5.power);
            jsonArray.put(DcMotorMaster.motorImpl6.power);
            jsonArray.put(DcMotorMaster.motorImpl7.power);
            jsonArray.put(DcMotorMaster.motorImpl8.power);
            jsonObject.put("motorPowers", jsonArray);

            //send json object to server

            unityPeerConnection.sendMessage(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
