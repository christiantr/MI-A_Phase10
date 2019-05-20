package com.mia.phase10.network;

import java.io.Serializable;

public class TextTransportObject implements Serializable {
    private final String message;

    public TextTransportObject(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "TextTransportObject{" +
                "message='" + message + '\'' +
                '}';
    }
}
