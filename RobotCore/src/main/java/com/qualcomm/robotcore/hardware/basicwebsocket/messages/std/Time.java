package com.qualcomm.robotcore.hardware.basicwebsocket.messages.std;

import com.qualcomm.robotcore.hardware.basicwebsocket.messages.Message;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * The std_msgs/Time message.
 * 
 * @author Russell Toris -- russell.toris@gmail.com
 * @version April 1, 2014
 */
public class Time extends Message {

	/**
	 * The name of the data field for the message.
	 */
	public static final java.lang.String FIELD_DATA = "data";

	/**
	 * The message type.
	 */
	public static final java.lang.String TYPE = "std_msgs/Time";

	private final com.qualcomm.robotcore.hardware.basicwebsocket.primitives.Time data;

	/**
	 * Create a new Time with a default of 0.
	 */
	public Time() {
		this(new com.qualcomm.robotcore.hardware.basicwebsocket.primitives.Time());
	}

	/**
	 * Create a new Time with the given time primitive.
	 *
	 * @param data
	 *            The data value of this time.
	 */
	public Time(com.qualcomm.robotcore.hardware.basicwebsocket.primitives.Time data) {
		// build the JSON object
		super(Json.createObjectBuilder()
				.add(Time.FIELD_DATA, data.toJsonObject()).build(), Time.TYPE);
		this.data = data;
	}

	/**
	 * Get the data value of this Time.
	 *
	 * @return The data value of this Time.
	 */
	public com.qualcomm.robotcore.hardware.basicwebsocket.primitives.Time getData() {
		return this.data;
	}

	/**
	 * Create a clone of this Time.
	 */
	@Override
	public Time clone() {
		// time objects are mutable, create a clone
		return new Time(this.data.clone());
	}

	/**
	 * Create a new Time based on the given JSON string. Any missing values will
	 * be set to their defaults.
	 *
	 * @param jsonString
	 *            The JSON string to parse.
	 * @return A Time message based on the given JSON string.
	 */
	public static Time fromJsonString(java.lang.String jsonString) {
		// convert to a message
		return Time.fromMessage(new Message(jsonString));
	}

	/**
	 * Create a new Time based on the given Message. Any missing values will be
	 * set to their defaults.
	 * 
	 * @param m
	 *            The Message to parse.
	 * @return A Time message based on the given Message.
	 */
	public static Time fromMessage(Message m) {
		// get it from the JSON object
		return Time.fromJsonObject(m.toJsonObject());
	}

	/**
	 * Create a new Time based on the given JSON object. Any missing values will
	 * be set to their defaults.
	 * 
	 * @param jsonObject
	 *            The JSON object to parse.
	 * @return A Time message based on the given JSON object.
	 */
	public static Time fromJsonObject(JsonObject jsonObject) {
		// check the fields
		com.qualcomm.robotcore.hardware.basicwebsocket.primitives.Time data = jsonObject
				.containsKey(Time.FIELD_DATA) ? com.qualcomm.robotcore.hardware.basicwebsocket.primitives.Time
				.fromJsonObject(jsonObject.getJsonObject(Time.FIELD_DATA))
				: new com.qualcomm.robotcore.hardware.basicwebsocket.primitives.Time();
		return new Time(data);
	}
}
