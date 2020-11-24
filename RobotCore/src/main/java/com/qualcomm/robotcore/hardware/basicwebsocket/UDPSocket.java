package com.qualcomm.robotcore.hardware.basicwebsocket;

import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSocket {

    private DatagramSocket socket;
    private InetAddress address;
    private int port;

    public UDPSocket(String ip, int port) {
        try {
            address = InetAddress.getByName(ip);
            socket = new DatagramSocket();
            this.port = port;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        receiveData();
    }


    public void send(String send) {
        try {
            System.out.println("message: " + send);
            DatagramPacket message = new DatagramPacket(send.getBytes(), send.getBytes().length, address, port);
            socket.send(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        socket.disconnect();
    }

    public void receiveData() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        byte[] buffer = new byte[1024];
                        DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                        socket.receive(response);
                        System.out.println("received response");
                        onMessage(new String(buffer, 0, response.getLength()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    public void onMessage(String message) {

    }
}
