package com.qualcomm.robotcore.hardware.basicwebsocket;

import org.java_websocket.handshake.ServerHandshake;

import java.io.StringReader;
import java.net.URI;
import java.nio.ByteBuffer;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonParsingException;

public class UdpRos extends UDPSocket {

    RosUDP ros;

    public UdpRos(String ip, int port, RosUDP ros) {
        super(ip, port);
        this.ros = ros;
    }

    @Override
    public void onMessage(String message) {
        try {
            // parse the JSON
            JsonObject jsonObject = Json
                    .createReader(new StringReader(message)).readObject();

            // check for compression
            String op = jsonObject.getString(JRosbridge.FIELD_OP);

            this.ros.handleMessage(jsonObject);

        } catch (NullPointerException | JsonParsingException e) {
            // only occurs if there was an error with the JSON
            System.err.println("[WARN]: Invalid incoming rosbridge protocol: "
                    + message);
        }
    }
}
