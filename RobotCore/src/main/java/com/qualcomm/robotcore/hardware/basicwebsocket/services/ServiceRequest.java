package com.qualcomm.robotcore.hardware.basicwebsocket.services;

import com.qualcomm.robotcore.hardware.basicwebsocket.JsonWrapper;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * ServiceRequest objects are used for making a request to a service. These
 * service requests act as wrappers around JSON objects. Service request data is
 * immutable.
 * 
 * @author Russell Toris - russell.toris@gmail.com
 * @version April 1, 2014
 */
public class ServiceRequest extends JsonWrapper {

	/**
	 * The String representation of an empty service request in JSON.
	 */
	public static final String EMPTY_MESSAGE = JsonWrapper.EMPTY_JSON;

	private String serviceRequestType;
	private String id;

	/**
	 * Create a new, empty service request. The type will be set to the empty
	 * string.
	 */
	public ServiceRequest() {
		this(ServiceRequest.EMPTY_MESSAGE, "");
	}

	/**
	 * Create a ServiceRequest based on the given String representation of a
	 * JSON object. The type will be set to the empty string.
	 * 
	 * @param jsonString
	 *            The JSON String to parse.
	 */
	public ServiceRequest(String jsonString) {
		this(jsonString, "");
	}

	/**
	 * Create a ServiceRequest based on the given String representation of a
	 * JSON object.
	 *
	 * @param jsonString
	 *            The JSON String to parse.
	 * @param serviceRequestType
	 *            The type of the service request (e.g., "std_srvs/Empty").
	 */
	public ServiceRequest(String jsonString, String serviceRequestType) {
		// parse and pass it to the JSON constructor
		this(Json.createReader(new StringReader(jsonString)).readObject(),
				serviceRequestType);
	}

	/**
	 * Create a ServiceRequest based on the given JSON object. The type will be
	 * set to the empty string.
	 * 
	 * @param jsonObject
	 *            The JSON object containing the service request data.
	 */
	public ServiceRequest(JsonObject jsonObject) {
		// setup the JSON information
		this(jsonObject, "");
	}

	/**
	 * Create a ServiceRequest based on the given JSON object.
	 *
	 * @param jsonObject
	 *            The JSON object containing the service request data.
	 * @param serviceRequestType
	 *            The type of the service request (e.g., "std_srvs/Empty").
	 */
	public ServiceRequest(JsonObject jsonObject, String serviceRequestType) {
		// setup the JSON information
		super(jsonObject);
		// set the type
		this.serviceRequestType = serviceRequestType;
		this.id = "";
	}

	/**
	 * Get the type of the service request if one was set.
	 * 
	 * @return The type of the service request.
	 */
	public String getServiceRequestType() {
		return this.serviceRequestType;
	}

	/**
	 * Set the type of the service request.
	 *
	 * @param serviceRequestType
	 *            The type of the service request (e.g., "std_srvs/Empty").
	 */
	public void setServiceRequestType(String serviceRequestType) {
		this.serviceRequestType = serviceRequestType;
	}

	/**
	 * Set the ID of the service request.
	 *
	 * @param id
	 *            The new ID.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the ID of the service request.
	 *
	 * @return The ID.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Create a clone of this ServiceRequest.
	 */
	@Override
	public ServiceRequest clone() {
		return new ServiceRequest(this.toJsonObject(), this.serviceRequestType);
	}
}
