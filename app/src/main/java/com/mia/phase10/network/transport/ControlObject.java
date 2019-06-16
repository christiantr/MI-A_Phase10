package com.mia.phase10.network.transport;

import java.io.Serializable;
import java.util.Objects;

public class ControlObject implements Serializable {
    private final ControlCommand controlCommand;
    private static final String CONTROL_COMMAND_NOT_NULL = "Control Command must not be NULL";

    private ControlObject(ControlCommand controlCommand) {
        Objects.requireNonNull(controlCommand, CONTROL_COMMAND_NOT_NULL);
        this.controlCommand = controlCommand;

    }

    public static ControlObject startGame() {
        return new ControlObject(ControlCommand.STARTGAME);
    }

    public static ControlObject closeConnections() {
        return new ControlObject(ControlCommand.CLOSECONNECTIONS);
    }

    public static ControlObject alertUsers() {
        return new ControlObject(ControlCommand.ALERTUSERS);
    }

    public ControlCommand getControlCommand() {
        return controlCommand;
    }
}
