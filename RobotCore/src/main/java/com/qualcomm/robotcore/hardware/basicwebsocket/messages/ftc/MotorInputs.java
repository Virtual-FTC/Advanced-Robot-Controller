package com.qualcomm.robotcore.hardware.basicwebsocket.messages.ftc;

import com.qualcomm.robotcore.hardware.basicwebsocket.messages.Message;
import javax.json.Json;
import javax.json.JsonObject;

public class MotorInputs extends Message {
    /**
     * The message type.
     */
    public static final String TYPE = "ftc_msgs/MotorInputs";

    private final DcMotorInput motor1;
    private final DcMotorInput motor2;
    private final DcMotorInput motor3;
    private final DcMotorInput motor4;
    private final DcMotorInput motor5;
    private final DcMotorInput motor6;
    private final DcMotorInput motor7;
    private final DcMotorInput motor8;

    public MotorInputs() {
        this(new DcMotorInput(), new DcMotorInput(), new DcMotorInput(), new DcMotorInput(), new DcMotorInput(), new DcMotorInput(), new DcMotorInput(), new DcMotorInput());
    }

    public MotorInputs(DcMotorInput motor1, DcMotorInput motor2, DcMotorInput motor3, DcMotorInput motor4, DcMotorInput motor5, DcMotorInput motor6, DcMotorInput motor7, DcMotorInput motor8) {
        // build the JSON object
        super(Json.createObjectBuilder()
                        .add("motor1", motor1.toJsonObject())
                        .add("motor2", motor2.toJsonObject())
                        .add("motor3", motor3.toJsonObject())
                        .add("motor4", motor4.toJsonObject())
                        .add("motor5", motor5.toJsonObject())
                        .add("motor6", motor6.toJsonObject())
                        .add("motor7", motor7.toJsonObject())
                        .add("motor8", motor8.toJsonObject()).build(),
                MotorInputs.TYPE);
        this.motor1 = motor1;
        this.motor2 = motor2;
        this.motor3 = motor3;
        this.motor4 = motor4;
        this.motor5 = motor5;
        this.motor6 = motor6;
        this.motor7 = motor7;
        this.motor8 = motor8;
    }


    public DcMotorInput getMotor1() {
        return this.motor1;
    }
    public DcMotorInput getMotor2() {
        return this.motor2;
    }
    public DcMotorInput getMotor3() {
        return this.motor3;
    }
    public DcMotorInput getMotor4() {
        return this.motor4;
    }
    public DcMotorInput getMotor5() {
        return this.motor5;
    }
    public DcMotorInput getMotor6() {
        return this.motor6;
    }
    public DcMotorInput getMotor7() {
        return this.motor7;
    }
    public DcMotorInput getMotor8() {
        return this.motor8;
    }

    /**
     * Create a clone of MotorInputs.
     */
    @Override
    public MotorInputs clone() {
        return new MotorInputs(new DcMotorInput(), new DcMotorInput(), new DcMotorInput(), new DcMotorInput(), new DcMotorInput(), new DcMotorInput(), new DcMotorInput(), new DcMotorInput());
    }


    public static MotorInputs fromJsonString(String jsonString) {
        // convert to a message
        return MotorInputs.fromMessage(new Message(jsonString));
    }


    public static MotorInputs fromMessage(Message m) {
        // get it from the JSON object
        return MotorInputs.fromJsonObject(m.toJsonObject());
    }


    public static MotorInputs fromJsonObject(JsonObject jsonObject) {
        // check the fields
        DcMotorInput motor1 = jsonObject.containsKey("motor1") ? DcMotorInput
                .fromJsonObject(jsonObject.getJsonObject("motor1"))
                : new DcMotorInput();
        DcMotorInput motor2 = jsonObject.containsKey("motor2") ? DcMotorInput
                .fromJsonObject(jsonObject.getJsonObject("motor2"))
                : new DcMotorInput();
        DcMotorInput motor3 = jsonObject.containsKey("motor3") ? DcMotorInput
                .fromJsonObject(jsonObject.getJsonObject("motor3"))
                : new DcMotorInput();
        DcMotorInput motor4 = jsonObject.containsKey("motor4") ? DcMotorInput
                .fromJsonObject(jsonObject.getJsonObject("motor4"))
                : new DcMotorInput();
        DcMotorInput motor5 = jsonObject.containsKey("motor5") ? DcMotorInput
                .fromJsonObject(jsonObject.getJsonObject("motor5"))
                : new DcMotorInput();
        DcMotorInput motor6 = jsonObject.containsKey("motor6") ? DcMotorInput
                .fromJsonObject(jsonObject.getJsonObject("motor6"))
                : new DcMotorInput();
        DcMotorInput motor7 = jsonObject.containsKey("motor7") ? DcMotorInput
                .fromJsonObject(jsonObject.getJsonObject("motor7"))
                : new DcMotorInput();
        DcMotorInput motor8 = jsonObject.containsKey("motor8") ? DcMotorInput
                .fromJsonObject(jsonObject.getJsonObject("motor8"))
                : new DcMotorInput();

        return new MotorInputs(motor1, motor2, motor3, motor4, motor5, motor6, motor7, motor8);
    }
}
