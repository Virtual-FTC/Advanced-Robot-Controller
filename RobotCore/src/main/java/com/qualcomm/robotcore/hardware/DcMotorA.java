package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.basicwebsocket.Ros;
import com.qualcomm.robotcore.hardware.basicwebsocket.Topic;
import com.qualcomm.robotcore.hardware.basicwebsocket.callback.TopicCallback;
import com.qualcomm.robotcore.hardware.basicwebsocket.messages.Message;
import com.qualcomm.robotcore.hardware.basicwebsocket.messages.ftc.DcMotorInput;
import java.net.URI;
import java.net.URISyntaxException;

import javax.json.JsonObject;

public class DcMotorA {
    public static String rosIp = "34.122.98.19";

    Ros client = null;

    Topic motorPub = null;
    Topic motorOutputPub = null;

    private double encoderPosition;

    double power = 0.0;

    public DcMotorA(String motorName) {
        try {
            client = new Ros(new URI("ws://" + rosIp + ":9091"));
            client.connect();
        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
        motorPub = new Topic(client, "/unity/" + motorName + "/input", "ftc_msgs/DcMotorInput");
        DcMotorInput toSend = new DcMotorInput(power, "");
        motorPub.publish(toSend);

        motorOutputPub = new Topic(client, "/unity/" + motorName + "/output", "ftc_msgs/DcMotorOutput");
        motorOutputPub.subscribe(new TopicCallback() {
            @Override
            public void handleMessage(Message message) {
                JsonObject data = message.toJsonObject();
                if(data.getJsonNumber("encoder_data").doubleValue() != 0) {
                    encoderPosition = data.getJsonNumber("encoder_data").doubleValue();
                }
            }
        });
    }

    public double getCurrentPosition(){
        return encoderPosition;
    }

    ZeroPowerBehavior zeroPowerBehavior;
    RunMode runMode;
    Direction direction;

    public enum ZeroPowerBehavior {
        BRAKE, FLOAT
    }

    public enum RunMode {
        RUN_WITHOUT_ENCODER, RUN_USING_ENCODER, RUN_TO_POSITION, STOP_AND_RESET_ENCODER
    }

    public enum Direction {
        FORWARD, REVERSE
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setZeroPowerBehavior (ZeroPowerBehavior zeroPowerBehavior) {
        this.zeroPowerBehavior = zeroPowerBehavior;
    }

    public void setMode(RunMode runMode) {
        this.runMode = runMode;
    }

    public void setPower(double power) {
        this.power = power;
        publishCmdVel();
    }

    public void publishCmdVel(){
        DcMotorInput dcMotorInputToSend = new DcMotorInput(power, "");
//        System.out.println("toSend: " +  dcMotorInputToSend.getCmd() + ", " + dcMotorInputToSend.getMode());
        motorPub.publish(dcMotorInputToSend);
    }
}
