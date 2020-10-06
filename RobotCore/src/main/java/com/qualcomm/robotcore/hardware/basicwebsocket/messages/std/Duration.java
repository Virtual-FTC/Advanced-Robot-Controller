package com.qualcomm.robotcore.hardware.basicwebsocket.messages.std;

import com.qualcomm.robotcore.hardware.basicwebsocket.messages.Message;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * The std_msgs/Duration message.
 * 
 * @author Russell Toris -- russell.toris@gmail.com
 * @version April 1, 2014
 */
public class Duration extends Message {

	/**
	 * The name of the data field for the message.
	 */
	public static final java.lang.String FIELD_DATA = "data";

	/**
	 * The message type.
	 */
	public static final java.lang.String TYPE = "std_msgs/Duration";

	private final com.qualcomm.robotcore.hardware.basicwebsocket.primitives.Duration data;

	/**
	 * Create a new Duration with a default of 0.
	 */
	public Duration() {
		this(new com.qualcomm.robotcore.hardware.basicwebsocket.primitives.Duration());
	}

	/**
	 * Create a new Duration with the given duration primitive.
	 *
	 * @param data
	 *            The data value of this duration.
	 */
	public Duration(com.qualcomm.robotcore.hardware.basicwebsocket.primitives.Duration data) {
		// build the JSON object
		super(Json.createObjectBuilder()
				.add(Duration.FIELD_DATA, data.toJsonObject()).build(),
				Duration.TYPE);
		this.data = data;
	}

	/**
	 * Get the data value of this Duration.
	 *
	 * @return The data value of this Duration.
	 */
	public com.qualcomm.robotcore.hardware.basicwebsocket.primitives.Duration getData() {
		return this.data;
	}

	/**
	 * Create a clone of this Duration.
	 */
	@Override
	public Duration clone() {
		// duration objects are mutable, create a clone
		return new Duration(this.data.clone());
	}

	/**
	 * Create a new Duration based on the given JSON string. Any missing values
	 * will be set to their defaults.
	 *
	 * @param jsonString
	 *            The JSON string to parse.
	 * @return A Duration message based on the given JSON string.
	 */
	public static Duration fromJsonString(java.lang.String jsonString) {
		// convert to a message
		return Duration.fromMessage(new Message(jsonString));
	}

	/**
	 * Create a new Duration based on the given Message. Any missing values will
	 * be set to their defaults.
	 * 
	 * @param m
	 *            The Message to parse.
	 * @return A Duration message based on the given Message.
	 */
	public static Duration fromMessage(Message m) {
		// get it from the JSON object
		return Duration.fromJsonObject(m.toJsonObject());
	}

	/**
	 * Create a new Duration based on the given JSON object. Any missing values
	 * will be set to their defaults.
	 * 
	 * @param jsonObject
	 *            The JSON object to parse.
	 * @return A Duration message based on the given JSON object.
	 */
	public static Duration fromJsonObject(JsonObject jsonObject) {
		// check the fields
		com.qualcomm.robotcore.hardware.basicwebsocket.primitives.Duration data = jsonObject
				.containsKey(Duration.FIELD_DATA) ? com.qualcomm.robotcore.hardware.basicwebsocket.primitives.Duration
				.fromJsonObject(jsonObject.getJsonObject(Duration.FIELD_DATA))
				: new com.qualcomm.robotcore.hardware.basicwebsocket.primitives.Duration();
		return new Duration(data);
	}
}
