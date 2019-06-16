package com.mia.phase10.network.transport;


import com.mia.phase10.gameLogic.GameLogicHandler;
import com.mia.phase10.network.ConnectionDetails;

import java.io.Serializable;
import java.util.Objects;

public class TransportObject implements Serializable {
    private final BroadcastType broadcastType;
    private final ObjectContentType objectContentType;
    private static final String BROADCAST_TYPE_NOT_NULL = "Broadcast type must not be NULL";
    private static final String CONTENT_TYPE_NOT_NULL = "Content type must not be NULL";
    private static final String PAYLOAD_NOT_NULL = "Payload must not be NULL";

    private final Serializable payload;

    private TransportObject(BroadcastType broadcastType, ObjectContentType objectContentType, Serializable payload) {
        Objects.requireNonNull(broadcastType, BROADCAST_TYPE_NOT_NULL);
        Objects.requireNonNull(objectContentType, CONTENT_TYPE_NOT_NULL);
        Objects.requireNonNull(payload, PAYLOAD_NOT_NULL);
        this.broadcastType = broadcastType;
        this.objectContentType = objectContentType;
        this.payload = payload;
    }

    public static TransportObject makeTextTransportObject(String text) {
        return new TransportObject(BroadcastType.BROADCAST_ALL, ObjectContentType.TEXT, text);
    }

    public static TransportObject ofControlObjectToAll(ControlObject controlObject) {
        return new TransportObject(BroadcastType.BROADCAST_ALL, ObjectContentType.CONTROLINFO, controlObject);
    }

    public static TransportObject ofControlObjectToThis(ControlObject controlObject) {
        return new TransportObject(BroadcastType.THISCLIENT, ObjectContentType.CONTROLINFO, controlObject);
    }

    public static TransportObject makeGameDataTransportObject(){
        return new TransportObject(BroadcastType.BROADCAST_ALL, ObjectContentType.GAMEDATA, GameLogicHandler.getInstance().getGameData());
    }

    public BroadcastType getBroadcastType() {
        return broadcastType;
    }

    public ObjectContentType getObjectContentType() {
        return objectContentType;
    }

    public Serializable getPayload() {
        return payload;
    }


    public static TransportObject tellUserName(ConnectionDetails connectionDetails) {
        return new TransportObject(BroadcastType.THISCLIENT, ObjectContentType.USERNAME, connectionDetails);
    }

    public static TransportObject setUserName(ConnectionDetails connectionDetails) {

        return new TransportObject(BroadcastType.HOSTONLY, ObjectContentType.USERNAME, connectionDetails);

    }

}
