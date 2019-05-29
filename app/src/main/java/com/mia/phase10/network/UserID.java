package com.mia.phase10.network;

public class UserID {
    private static int count = 0;
    private final int userId;

    private UserID(int userId) {
        this.userId = userId;
    }


    public int getUserId() {
        return userId;
    }


    public static UserID nextId() {
        count++;
        return new UserID(count);

    }
}
