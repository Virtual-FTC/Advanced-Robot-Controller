package com.qualcomm.robotcore.hardware.basicwebsocket.messages.ftc;


import com.qualcomm.robotcore.hardware.basicwebsocket.messages.Message;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * The geometry_msgs/DcMotorInput message. This expresses velocity in free space broken
 * into its linear and mode parts.
 *
 * @author Russell Toris -- russell.toris@gmail.com
 * @version April 1, 2014
 */
public class DcMotorInput extends Message {

    /**
     * The name of the linear field for the message.
     */
    public static final String FIELD_CMD = "cmd";

    /**
     * The name of the mode field for the message.
     */
    public static final String FIELD_MODE = "mode";

    /**
     * The message type.
     */
    public static final String TYPE = "ftc_msgs/DcMotorInput";

    private final double cmd;
    private final String mode;

    /**
     * Create a new DcMotorInput with all 0s.
     */
    public DcMotorInput() {
        this(0.0, "");
    }

    /**
     * Create a new DcMotorInput with the given cmd and mode values.
     *
     * @param cmd
     *            The cmd value of the twist.
     * @param mode
     *            The mode value of the twist.
     */
    public DcMotorInput(double cmd, String mode) {
        // build the JSON object
        super(Json.createObjectBuilder()
                        .add(DcMotorInput.FIELD_CMD, cmd)
                        .add(DcMotorInput.FIELD_MODE, mode).build(),
                DcMotorInput.TYPE);
        this.cmd = cmd;
        this.mode = mode;
    }

    /**
     * Get the cmd value of this twist.
     *
     * @return The cmd value of this twist.
     */
    public double getCmd() {
        return this.cmd;
    }

    /**
     * Get the mode value of this twist.
     *
     * @return The mode value of this twist.
     */
    public String getMode() {
        return this.mode;
    }

    /**
     * Create a clone of this DcMotorInput.
     */
    @Override
    public DcMotorInput clone() {
        return new DcMotorInput(this.cmd, this.mode);
    }

    /**
     * Create a new DcMotorInput based on the given JSON string. Any missing values
     * will be set to their defaults.
     *
     * @param jsonString
     *            The JSON string to parse.
     * @return A DcMotorInput message based on the given JSON string.
     */
    public static DcMotorInput fromJsonString(String jsonString) {
        // convert to a message
        return DcMotorInput.fromMessage(new Message(jsonString));
    }

    /**
     * Create a new DcMotorInput based on the given Message. Any missing values will be
     * set to their defaults.
     *
     * @param m
     *            The Message to parse.
     * @return A DcMotorInput message based on the given Message.
     */
    public static DcMotorInput fromMessage(Message m) {
        // get it from the JSON object
        return DcMotorInput.fromJsonObject(m.toJsonObject());
    }

    /**
     * Create a new DcMotorInput based on the given JSON object. Any missing values
     * will be set to their defaults.
     *
     * @param jsonObject
     *            The JSON object to parse.
     * @return A DcMotorInput message based on the given JSON object.
     */
    public static DcMotorInput fromJsonObject(JsonObject jsonObject) {
        // check the fields
        double cmd = jsonObject.containsKey(DcMotorInput.FIELD_CMD) ? jsonObject
                .getJsonNumber(DcMotorInput.FIELD_CMD).doubleValue() : 0.0;

        String mode = jsonObject.containsKey(DcMotorInput.FIELD_MODE) ? jsonObject
                .getString(DcMotorInput.FIELD_MODE) : "";
        return new DcMotorInput(cmd, mode);
    }
}