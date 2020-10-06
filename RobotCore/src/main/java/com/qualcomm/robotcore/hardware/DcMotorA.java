package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.basicwebsocket.*;
import com.qualcomm.robotcore.hardware.basicwebsocket.messages.geometry.Twist;
import com.qualcomm.robotcore.hardware.basicwebsocket.messages.geometry.Vector3;

import java.net.URI;
import java.net.URISyntaxException;

public class DcMotorA {
    String rosIp = "35.222.238.30";

    Ros client = null;

    Topic xVelPub = null;

    Vector3 linear = new Vector3(0,0,0);
    double linX = 0.0;
    double linY = 0.0;
    double linZ = 0.0;
    Vector3 angular = new Vector3(0,0,0);
    double angX = 0.0;
    double angY = 0.0;
    double angZ = 0.0;

    double xVel = 1.0;
    double thVel = 1.0;

    public DcMotorA() {
        try {
            client = new Ros(new URI("ws://" + rosIp + ":9091"));
            client.connect();
        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
        xVelPub = new Topic(client, "/cmd_vel", "geometry_msgs/Twist");
        Twist toSend = new Twist(linear, angular);
        xVelPub.publish(toSend);
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

    public void setLinVel(double x, double y) {
        linX = x;
        linY = y;
        publishCmdVel();
    }

    public void setTurn(double angleVel) {
        angZ = angleVel;
        publishCmdVel();
    }

    public void publishCmdVel(){
        linear = new Vector3(linX, linY, linZ);
        angular = new Vector3(angX, angY, angZ);
        Twist toSend = new Twist(linear, angular);
        xVelPub.publish(toSend);
    }
}
