package com.qualcomm.robotcore.hardware.basicwebsocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.StringReader;
import java.net.URI;
import java.nio.ByteBuffer;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonParsingException;

public class WebRos extends WebSocketClient {

    private Ros ros;

    public WebRos(URI serverURI, Ros ros) {
        super(serverURI);
        this.ros = ros;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("Hello, it is me. Mario :)");
        System.out.println("[INFO] new connection opened");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);
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

    @Override
    public void onMessage(ByteBuffer message) {
        System.out.println("received ByteBuffer");
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("an error occurred:" + ex);
    }

}
