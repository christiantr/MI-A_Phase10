package com.mia.phase10.network;

public class TextTransportObject {
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
