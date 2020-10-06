package com.qualcomm.robotcore.hardware.basicwebsocket.messages.std;

import com.qualcomm.robotcore.hardware.basicwebsocket.messages.Message;

import javax.json.JsonObject;

/**
 * The std_msgs/Empty message.
 * 
 * @author Russell Toris -- russell.toris@gmail.com
 * @version April 1, 2014
 */
public class Empty extends Message {

	/**
	 * The message type.
	 */
	public static final java.lang.String TYPE = "std_msgs/Empty";

	/**
	 * Create a new Empty message.
	 */
	public Empty() {
		super(Message.EMPTY_MESSAGE, Empty.TYPE);
	}

	/**
	 * Create a clone of this Empty.
	 */
	@Override
	public Empty clone() {
		return new Empty();
	}

	/**
	 * Create a new Empty based on the given JSON string. Any missing values
	 * will be set to their defaults.
	 *
	 * @param jsonString
	 *            The JSON string to parse.
	 * @return A Empty message based on the given JSON string.
	 */
	public static Empty fromJsonString(java.lang.String jsonString) {
		// convert to a message
		return Empty.fromMessage(new Message(jsonString));
	}

	/**
	 * Create a new Empty based on the given Message. Any missing values will be
	 * set to their defaults.
	 * 
	 * @param m
	 *            The Message to parse.
	 * @return A Empty message based on the given Message.
	 */
	public static Empty fromMessage(Message m) {
		// get it from the JSON object
		return Empty.fromJsonObject(m.toJsonObject());
	}

	/**
	 * Create a new Empty based on the given JSON object. Any missing values
	 * will be set to their defaults.
	 * 
	 * @param jsonObject
	 *            The JSON object to parse.
	 * @return A Empty message based on the given JSON object.
	 */
	public static Empty fromJsonObject(JsonObject jsonObject) {
		return new Empty();
	}
}
