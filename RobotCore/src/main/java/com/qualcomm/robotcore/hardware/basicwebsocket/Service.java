package com.qualcomm.robotcore.hardware.basicwebsocket;

import com.qualcomm.robotcore.hardware.basicwebsocket.callback.CallServiceCallback;
import com.qualcomm.robotcore.hardware.basicwebsocket.callback.ServiceCallback;
import com.qualcomm.robotcore.hardware.basicwebsocket.services.ServiceRequest;
import com.qualcomm.robotcore.hardware.basicwebsocket.services.ServiceResponse;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * The Service object is responsible for calling or advertising a service in ROS.
 * 
 * @author Russell Toris - russell.toris@gmail.com
 * @version November 26, 2014
 */
public class Service {

	private final Ros ros;
	private final String name;
	private final String type;
	private boolean isAdvertised;

	/**
	 * Create a ROS service with the given information.
	 * 
	 * @param ros
	 *            A handle to the ROS connection.
	 * @param name
	 *            The name of the service (e.g., "/add_two_ints").
	 * @param type
	 *            The service type (e.g., "rospy_tutorials/AddTwoInts").
	 */
	public Service(Ros ros, String name, String type) {
		this.ros = ros;
		this.name = name;
		this.type = type;
		this.isAdvertised = false;
	}

	/**
	 * Get the ROS connection handle for this service.
	 * 
	 * @return The ROS connection handle for this service.
	 */
	public Ros getRos() {
		return this.ros;
	}

	/**
	 * Get the name of this service.
	 * 
	 * @return The name of this service.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Return the service type of this service.
	 * 
	 * @return The service type of this service.
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Check if the current service is advertising to ROS.
	 *
	 * @return If the current service is advertising to ROS.
	 */
	public boolean isAdvertised() {
		return this.isAdvertised;
	}

	/**
	 * Call this service. The callback function will be called with the
	 * associated service response.
	 * 
	 * @param request
	 *            The service request to send.
	 * @param cb
	 *            The callback used when the associated response comes back.
	 */
	public void callService(ServiceRequest request, ServiceCallback cb) {
		// construct the unique ID
		String callServceId = "call_service:" + this.name + ":"
				+ this.ros.nextId();

		// register the callback function
		this.ros.registerServiceCallback(callServceId, cb);

		// build and send the rosbridge call
		JsonObject call = Json.createObjectBuilder()
				.add(JRosbridge.FIELD_OP, JRosbridge.OP_CODE_CALL_SERVICE)
				.add(JRosbridge.FIELD_ID, callServceId)
				.add(JRosbridge.FIELD_TYPE, this.type)
				.add(JRosbridge.FIELD_SERVICE, this.name)
				.add(JRosbridge.FIELD_ARGS, request.toJsonObject()).build();
		this.ros.sendJson(call);
	}

	/**
	 * Send a service response.
	 *
	 * @param response
	 *            The service response to send.
	 * @param id
	 *            The ID of the response (matching that of the service call).
	 */
	public void sendResponse(ServiceResponse response, String id) {
		// build and send the rosbridge call
		JsonObject call = Json.createObjectBuilder()
				.add(JRosbridge.FIELD_OP, JRosbridge.OP_CODE_SERVICE_RESPONSE)
				.add(JRosbridge.FIELD_ID, id)
				.add(JRosbridge.FIELD_SERVICE, this.name)
				.add(JRosbridge.FIELD_VALUES, response.toJsonObject())
				.add(JRosbridge.FIELD_RESULT, response.getResult()).build();
		this.ros.sendJson(call);
	}

	/**
	 * Registers as service advertiser.
	 */
	public void advertiseService(CallServiceCallback cb) {
		// register the callback
		this.ros.registerCallServiceCallback(this.name, cb);

		// build and send the rosbridge call
		JsonObject call = Json.createObjectBuilder()
				.add(JRosbridge.FIELD_OP, JRosbridge.OP_CODE_ADVERTISE_SERVICE)
				.add(JRosbridge.FIELD_TYPE, this.type)
				.add(JRosbridge.FIELD_SERVICE, this.name).build();
		this.ros.sendJson(call);

		// set the flag indicating we are registered
		this.isAdvertised = true;
	}

	/**
	 * Unregisters as service advertiser.
	 */
	public void unadvertiseService() {
		this.ros.deregisterCallServiceCallback(this.name);

		// build and send the rosbridge call
		JsonObject call = Json.createObjectBuilder()
				.add(JRosbridge.FIELD_OP, JRosbridge.OP_CODE_UNADVERTISE_SERVICE)
				.add(JRosbridge.FIELD_SERVICE, this.name).build();
		this.ros.sendJson(call);

		// set the flag indicating we are registered
		this.isAdvertised = false;
	}

	/**
	 * Call the service and wait for a response. This is a blocking call and
	 * will only return once rosbridge returns the service response. For an
	 * asynchronous version of this call, see the
	 * {@link #callService(ServiceRequest request, ServiceCallback cb)
	 * callService} method.
	 * 
	 * @param request
	 *            The service request to send.
	 * @return The corresponding service response from ROS.
	 */
	public synchronized ServiceResponse callServiceAndWait(
			ServiceRequest request) {

		// private inner class to use as a callback
		BlockingCallback cb = new BlockingCallback(this);
		// use the asynchronous version and block on the result
		this.callService(request, cb);

		// wait for a response
		while (cb.getResponse() == null) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// continue on
			}
		}

		return cb.getResponse();
	}

	/**
	 * A private {@link com.qualcomm.robotcore.hardware.basicwebsocket.callback.ServiceCallback
	 * ServiceCallback} used to block and wait for a response from rosbridge.
	 * 
	 * @author Russell Toris - russell.toris@gmail.com
	 * @version April 1, 2014
	 */
	private class BlockingCallback implements ServiceCallback {

		private ServiceResponse response;
		private Service service;

		/**
		 * Create a new callback function which will notify the given
		 * {@link com.qualcomm.robotcore.hardware.basicwebsocket.Service Service} once a response
		 * has been received.
		 * 
		 * @param service
		 *            The {@link com.qualcomm.robotcore.hardware.basicwebsocket.Service Service}
		 *            to notify once a response has been received.
		 */
		public BlockingCallback(Service service) {
			this.response = null;
			this.service = service;
		}

		/**
		 * Store the response internally and notify the corresponding
		 * {@link com.qualcomm.robotcore.hardware.basicwebsocket.Service Service}.
		 * 
		 * @param response
		 *            The incoming service response from ROS.
		 */
		@Override
		public void handleServiceResponse(ServiceResponse response) {
			this.response = response;
			synchronized (this.service) {
				this.service.notifyAll();
			}
		}

		/**
		 * Get the response stored in this callback, if one exists. Otherwise,
		 * null is returned.
		 * 
		 * @return The resulting service response from ROS, or null if one does
		 *         not exist yet.
		 */
		public ServiceResponse getResponse() {
			return this.response;
		}
	}
}
