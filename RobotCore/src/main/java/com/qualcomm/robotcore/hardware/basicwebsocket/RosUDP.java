
package com.qualcomm.robotcore.hardware.basicwebsocket;

import com.qualcomm.robotcore.hardware.basicwebsocket.callback.CallServiceCallback;
import com.qualcomm.robotcore.hardware.basicwebsocket.callback.ServiceCallback;
import com.qualcomm.robotcore.hardware.basicwebsocket.callback.TopicCallback;
import com.qualcomm.robotcore.hardware.basicwebsocket.messages.Message;
import com.qualcomm.robotcore.hardware.basicwebsocket.services.ServiceRequest;
import com.qualcomm.robotcore.hardware.basicwebsocket.services.ServiceResponse;

import org.java_websocket.client.WebSocketClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * The Ros object is the main connection point to the rosbridge server. This
 * object manages all communication to-and-from ROS. Typically, this object is
 * not used on its own. Instead, helper classes, such as
 * {@link com.qualcomm.robotcore.hardware.basicwebsocket.JRosbridge Topic}, are used.
 *
 * @author Russell Toris - russell.toris@gmail.com
 * @version April 1, 2014
 */

public class RosUDP {

    // used throughout the library to create unique IDs for requests.
    private long idCounter;

    // keeps track of callback functions for a given topic
    private final HashMap<String, ArrayList<TopicCallback>> topicCallbacks;

    // keeps track of callback functions for a given service request
    private final HashMap<String, ServiceCallback> serviceCallbacks;

    // keeps track of callback functions for a given advertised service
    private final HashMap<String, CallServiceCallback> callServiceCallbacks;

    private UDPSocket udpRos;


    public RosUDP(String serverIP, int port) {
        this.idCounter = 0;
        this.topicCallbacks = new HashMap<String, ArrayList<TopicCallback>>();
        this.serviceCallbacks = new HashMap<String, ServiceCallback>();
        this.callServiceCallbacks = new HashMap<String, CallServiceCallback>();
        this.udpRos = new UdpRos(serverIP, port, this);
    }

    public void connect() {
        this.udpRos.connect();
    }

    public void disconnect() {
        this.udpRos.disconnect();
    }

    /**
     * Get the next unique ID number for this connection.
     *
     * @return The next unique ID number for this connection.
     */
    public long nextId() {
        return this.idCounter++;
    }

    /**
     * Handle the incoming rosbridge message by calling the appropriate
     * callbacks.
     *
     * @param jsonObject The JSON object from the incoming rosbridge message.
     */
    public void handleMessage(JsonObject jsonObject) {
        // check for the correct fields
        String op = jsonObject.getString(JRosbridge.FIELD_OP);
        if (op.equals(JRosbridge.OP_CODE_PUBLISH)) {
            // check for the topic name
            String topic = jsonObject.getString(JRosbridge.FIELD_TOPIC);

            // call each callback with the message
            ArrayList<TopicCallback> callbacks = topicCallbacks.get(topic);
            if (callbacks != null) {
                Message msg = new Message(
                        jsonObject.getJsonObject(JRosbridge.FIELD_MESSAGE));
                for (TopicCallback cb : callbacks) {
                    cb.handleMessage(msg);
                }
            }
        } else if (op.equals(JRosbridge.OP_CODE_SERVICE_RESPONSE)) {
            // check for the request ID
            String id = jsonObject.getString(JRosbridge.FIELD_ID);

            // call the callback for the request
            ServiceCallback cb = serviceCallbacks.get(id);
            if (cb != null) {
                // check if a success code was given
                boolean success = jsonObject
                        .containsKey(JRosbridge.FIELD_RESULT) ? jsonObject
                        .getBoolean(JRosbridge.FIELD_RESULT) : true;
                // get the response
                JsonObject values = jsonObject
                        .getJsonObject(JRosbridge.FIELD_VALUES);
                ServiceResponse response = new ServiceResponse(values, success);
                cb.handleServiceResponse(response);
            }
        } else if (op.equals(JRosbridge.OP_CODE_CALL_SERVICE)) {
            // check for the request ID
            String id = jsonObject.getString("id");
            String service = jsonObject.getString("service");

            // call the callback for the request
            CallServiceCallback cb = callServiceCallbacks.get(service);
            if (cb != null) {
                // get the response
                JsonObject args = jsonObject
                        .getJsonObject(JRosbridge.FIELD_ARGS);
                ServiceRequest request = new ServiceRequest(args);
                request.setId(id);
                cb.handleServiceCall(request);
            }
        } else {
            System.err.println("[WARN]: Unrecognized op code: "
                    + jsonObject.toString());
        }

    }

    /**
     * Send the given JSON object to rosbridge.
     *
     * @param jsonObject The JSON object to send to rosbridge.
     * @return If the sending of the message was successful.
     */
    public boolean sendJson(JsonObject jsonObject) {
        // send it as text
        udpRos.send(jsonObject.toString());
        return true;
    }

    /**
     * Sends an authorization request to the server.
     *
     * @param mac    The MAC (hash) string given by the trusted source.
     * @param client The IP of the client.
     * @param dest   The IP of the destination.
     * @param rand   The random string given by the trusted source.
     * @param t      The time of the authorization request.
     * @param level  The user level as a string given by the client.
     * @param end    The end time of the client's session.
     */
    public void authenticate(String mac, String client, String dest,
                             String rand, int t, String level, int end) {
        // build and send the rosbridge call
        JsonObject call = Json.createObjectBuilder()
                .add(JRosbridge.FIELD_OP, JRosbridge.OP_CODE_AUTH)
                .add(JRosbridge.FIELD_MAC, mac)
                .add(JRosbridge.FIELD_CLIENT, client)
                .add(JRosbridge.FIELD_DESTINATION, dest)
                .add(JRosbridge.FIELD_RAND, rand).add(JRosbridge.FIELD_TIME, t)
                .add(JRosbridge.FIELD_LEVEL, level)
                .add(JRosbridge.FIELD_END_TIME, end).build();
        this.sendJson(call);
    }

    /**
     * Register a callback for a given topic.
     *
     * @param topic The topic to register this callback with.
     * @param cb    The callback that will be called when messages come in for the
     *              associated topic.
     */
    public void registerTopicCallback(String topic, TopicCallback cb) {
        // check if any callbacks exist yet
        if (!this.topicCallbacks.containsKey(topic)) {
            this.topicCallbacks.put(topic, new ArrayList<TopicCallback>());
        }

        // add the callback
        this.topicCallbacks.get(topic).add(cb);
    }

    /**
     * Deregister a callback for a given topic.
     *
     * @param topic The topic associated with the callback.
     * @param cb    The callback to remove.
     */
    public void deregisterTopicCallback(String topic, TopicCallback cb) {
        // check if any exist for this topic
        if (this.topicCallbacks.containsKey(topic)) {
            // remove the callback if it exists
            ArrayList<TopicCallback> callbacks = this.topicCallbacks.get(topic);
            if (callbacks.contains(cb)) {
                callbacks.remove(cb);
            }

            // remove the list if it is empty
            if (callbacks.size() == 0) {
                this.topicCallbacks.remove(topic);
            }
        }
    }

    /**
     * Register a callback for a given outgoing service call.
     *
     * @param serviceCallId The unique ID of the service call.
     * @param cb            The callback that will be called when a service response comes
     *                      back for the associated request.
     */
    public void registerServiceCallback(String serviceCallId, ServiceCallback cb) {
        // add the callback
        this.serviceCallbacks.put(serviceCallId, cb);
    }

    /**
     * Register a callback for a given incoming service request.
     *
     * @param serviceName The unique name of the service call.
     * @param cb          The callback that will be called when a service request comes
     *                    in for the associated request.
     */
    public void registerCallServiceCallback(String serviceName, CallServiceCallback cb) {
        // add the callback
        this.callServiceCallbacks.put(serviceName, cb);
    }

    /**
     * Deregister a callback for a given incoming service request.
     *
     * @param serviceName The unique name of the service call.
     */
    public void deregisterCallServiceCallback(String serviceName) {
        // remove the callback
        callServiceCallbacks.remove(serviceName);
    }
}
