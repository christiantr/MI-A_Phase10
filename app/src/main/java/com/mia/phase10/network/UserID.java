package com.mia.phase10.network;

import java.io.Serializable;
import java.util.Objects;

public class UserID implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserID userID = (UserID) o;
        return userId == userID.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
