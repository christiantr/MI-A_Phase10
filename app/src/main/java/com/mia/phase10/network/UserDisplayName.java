package com.mia.phase10.network;

import java.io.Serializable;

public class UserDisplayName implements Serializable {
    private final String name;

    public String getName() {
        return name;
    }

    public UserDisplayName(String name) {
        this.name = name;
    }
}
