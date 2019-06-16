package com.mia.phase10.network;

import java.io.Serializable;
import java.util.Objects;

public class UserID implements Serializable {
    private static int count = 0;
    private final int identification;

    private UserID(int identification) {
        this.identification = identification;
    }


    public int getIdentification() {
        return identification;
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
        return identification == userID.identification;
    }

    @Override
    public int hashCode() {
        return Objects.hash(identification);
    }

    public int compare(UserID other) {
        return Integer.compare(other.identification, this.identification);
    }
}
