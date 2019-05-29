package com.mia.phase10.network;

public class ConnectionDetails {
    private final UserID userID;
    private UserDisplayName userDisplayName;

    private ConnectionDetails(UserID userID, UserDisplayName userDisplayName) {
        this.userID = userID;
        this.userDisplayName = userDisplayName;
    }

    public static ConnectionDetails makeNext() {
        UserID userID = UserID.nextId();
        UserDisplayName userDisplayName = new UserDisplayName(String.format("User %d", userID.getUserId()));

        return new ConnectionDetails(userID, userDisplayName);
    }

    public UserID getUserID() {
        return userID;
    }

    public UserDisplayName getUserDisplayName() {
        return userDisplayName;
    }
}
