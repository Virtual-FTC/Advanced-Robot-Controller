package com.qualcomm.robotcore.hardware.basicwebsocket.messages.std;

import com.qualcomm.robotcore.hardware.basicwebsocket.messages.Message;

import java.io.StringReader;
import java.util.Arrays;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 * The std_msgs/Int32MultiArray message. Please look at the MultiArrayLayout
 * message definition for documentation on all multiarrays.
 * 
 * @author Russell Toris -- russell.toris@gmail.com
 * @version April 1, 2014
 */
public class Int32MultiArray extends Message {

	/**
	 * The name of the layout field for the message.
	 */
	public static final java.lang.String FIELD_LAYOUT = "layout";

	/**
	 * The name of the data field for the message.
	 */
	public static final java.lang.String FIELD_DATA = "data";

	/**
	 * The message type.
	 */
	public static final java.lang.String TYPE = "std_msgs/Int32MultiArray";

	private final MultiArrayLayout layout;
	private final int[] data;

	/**
	 * Create a new, empty Int32MultiArray.
	 */
	public Int32MultiArray() {
		this(new MultiArrayLayout(), new int[] {});
	}

	/**
	 * Create a new Int32MultiArray with the given layout and data. The array of
	 * data will be copied into this object.
	 *
	 * @param layout
	 *            The specification of data layout.
	 * @param data
	 *            The array of data.
	 */
	public Int32MultiArray(MultiArrayLayout layout, int[] data) {
		// build the JSON object
		super(Json
				.createObjectBuilder()
				.add(Int32MultiArray.FIELD_LAYOUT, layout.toJsonObject())
				.add(Int32MultiArray.FIELD_DATA,
						Json.createReader(
								new StringReader(Arrays.toString(data)))
								.readArray()).build(), Int32MultiArray.TYPE);
		this.layout = layout;
		// copy the array
		this.data = new int[data.length];
		System.arraycopy(data, 0, this.data, 0, data.length);
	}

	/**
	 * Get the layout value of this Int32MultiArray.
	 *
	 * @return The layout value of this Int32MultiArray.
	 */
	public MultiArrayLayout getLayout() {
		return this.layout;
	}

	/**
	 * Get the size of the array.
	 *
	 * @return The size of the array.
	 */
	public int size() {
		return this.data.length;
	}

	/**
	 * Get the data value at the given index.
	 *
	 * @param index
	 *            The index to get the data value of.
	 * @return The data value at the given index.
	 */
	public int get(int index) {
		return this.data[index];
	}

	/**
	 * Get the data array. Note that this array should never be modified
	 * directly.
	 *
	 * @return The data array.
	 */
	public int[] getData() {
		return this.data;
	}

	/**
	 * Create a clone of this Int32MultiArray.
	 */
	@Override
	public Int32MultiArray clone() {
		return new Int32MultiArray(this.layout, this.data);
	}

	/**
	 * Create a new Int32MultiArray based on the given JSON string. Any missing
	 * values will be set to their defaults.
	 *
	 * @param jsonString
	 *            The JSON string to parse.
	 * @return A Int32MultiArray message based on the given JSON string.
	 */
	public static Int32MultiArray fromJsonString(java.lang.String jsonString) {
		// convert to a message
		return Int32MultiArray.fromMessage(new Message(jsonString));
	}

	/**
	 * Create a new Int32MultiArray based on the given Message. Any missing
	 * values will be set to their defaults.
	 * 
	 * @param m
	 *            The Message to parse.
	 * @return A Int32MultiArray message based on the given Message.
	 */
	public static Int32MultiArray fromMessage(Message m) {
		// get it from the JSON object
		return Int32MultiArray.fromJsonObject(m.toJsonObject());
	}

	/**
	 * Create a new Int32MultiArray based on the given JSON object. Any missing
	 * values will be set to their defaults.
	 * 
	 * @param jsonObject
	 *            The JSON object to parse.
	 * @return A Int32MultiArray message based on the given JSON object.
	 */
	public static Int32MultiArray fromJsonObject(JsonObject jsonObject) {
		// check the layout
		MultiArrayLayout layout = jsonObject
				.containsKey(Int32MultiArray.FIELD_LAYOUT) ? MultiArrayLayout
				.fromJsonObject(jsonObject
						.getJsonObject(Int32MultiArray.FIELD_LAYOUT))
				: new MultiArrayLayout();

		// check the array
		int[] data = new int[] {};
		JsonArray jsonData = jsonObject
				.getJsonArray(Int32MultiArray.FIELD_DATA);
		if (jsonData != null) {
			// convert each data
			data = new int[jsonData.size()];
			for (int i = 0; i < data.length; i++) {
				data[i] = jsonData.getInt(i);
			}
		}
		return new Int32MultiArray(layout, data);
	}
}
