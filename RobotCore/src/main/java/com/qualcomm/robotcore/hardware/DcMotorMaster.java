package com.qualcomm.robotcore.hardware;


public class DcMotorMaster {

    /**
     * DcMotorImpl Objects
     */

    public static DcMotorImpl motorImpl1;
    public static DcMotorImpl motorImpl2;
    public static DcMotorImpl motorImpl3;
    public static DcMotorImpl motorImpl4;
    public static DcMotorImpl motorImpl5;
    public static DcMotorImpl motorImpl6;
    public static DcMotorImpl motorImpl7;
    public static DcMotorImpl motorImpl8;

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


//    private static DatagramSocket socket;

    public static void start() {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    String address = "192.168.1.49";
//                    int port = 9050;
//                    socket = new DatagramSocket();
//                    socket.connect(InetAddress.getByName(address), port);
//                    while (true) {
//                        Thread.sleep(30);
//                        sendMotorPowers();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.setPriority(Thread.MAX_PRIORITY);
//        thread.start();
    }

//    private static void sendMotorPowers() {
//        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("motor1", DcMotorMaster.motorImpl1.power);
//            jsonObject.put("motor2", DcMotorMaster.motorImpl2.power);
//            jsonObject.put("motor3", DcMotorMaster.motorImpl3.power);
//            jsonObject.put("motor4", DcMotorMaster.motorImpl4.power);
//
//            String message = jsonObject.toString();
//            System.out.println("message: " + message);
//            socket.send(new DatagramPacket(message.getBytes(), message.length()));
////            byte[] buffer = new byte[1024];
////            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
////            socket.receive(response);
////            System.out.println("RESPONSE: " + new String(buffer, 0, response.getLength()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
