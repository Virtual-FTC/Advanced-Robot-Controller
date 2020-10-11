package com.qualcomm.robotcore.hardware.basicwebsocket.messages.ftc;


import com.qualcomm.robotcore.hardware.basicwebsocket.messages.Message;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * The geometry_msgs/DcMotorOutput message. This expresses velocity in free space broken
 * into its linear and mode parts.
 *
 * @author Russell Toris -- russell.toris@gmail.com
 * @version April 1, 2014
 */
public class DcMotorOutput extends Message {

    /**
     * The name of the linear field for the message.
     */
    public static final java.lang.String FIELD_ENCODER_DATA = "encoder_data";

    /**
     * The message type.
     */
    public static final java.lang.String TYPE = "ftc_msgs/DcMotorOutput";

    private final double encoder_data;

    /**
     * Create a new DcMotorOutput with all 0s.
     */
    public DcMotorOutput() {
        this(0.0);
    }

    /**
     * Create a new DcMotorOutput with the given cmd and mode values.
     *
     * @param encoder_data
     *            The cmd value of the twist.
     */
    public DcMotorOutput(double encoder_data) {
        // build the JSON object
        super(Json.createObjectBuilder()
                        .add(DcMotorOutput.FIELD_ENCODER_DATA, encoder_data).build(),
                DcMotorOutput.TYPE);
        this.encoder_data = encoder_data;
    }

    /**
     * Get the encoder_data value of this twist.
     *
     * @return The encoder_data value of this twist.
     */
    public double getEncoderData() {
        return this.encoder_data;
    }

    /**
     * Create a clone of this DcMotorOutput.
     */
    @Override
    public DcMotorOutput clone() {
        return new DcMotorOutput(this.encoder_data);
    }

    /**
     * Create a new DcMotorOutput based on the given JSON string. Any missing values
     * will be set to their defaults.
     *
     * @param jsonString
     *            The JSON string to parse.
     * @return A DcMotorOutput message based on the given JSON string.
     */
    public static DcMotorOutput fromJsonString(java.lang.String jsonString) {
        // convert to a message
        return DcMotorOutput.fromMessage(new Message(jsonString));
    }

    /**
     * Create a new DcMotorOutput based on the given Message. Any missing values will be
     * set to their defaults.
     *
     * @param m
     *            The Message to parse.
     * @return A DcMotorOutput message based on the given Message.
     */
    public static DcMotorOutput fromMessage(Message m) {
        // get it from the JSON object
        return DcMotorOutput.fromJsonObject(m.toJsonObject());
    }

    /**
     * Create a new DcMotorOutput based on the given JSON object. Any missing values
     * will be set to their defaults.
     *
     * @param jsonObject
     *            The JSON object to parse.
     * @return A DcMotorOutput message based on the given JSON object.
     */
    public static DcMotorOutput fromJsonObject(JsonObject jsonObject) {
        // check the fields
        double encoder_data = jsonObject.containsKey(DcMotorOutput.FIELD_ENCODER_DATA) ? jsonObject
                .getJsonNumber(DcMotorOutput.FIELD_ENCODER_DATA).doubleValue() : 0.0;
        return new DcMotorOutput(encoder_data);
    }
}