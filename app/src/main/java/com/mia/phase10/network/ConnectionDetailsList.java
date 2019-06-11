package com.mia.phase10.network;

import java.util.ArrayList;
import java.util.List;

public class ConnectionDetailsList {
    private List<ConnectionDetails> connectionDetailsList;


    public static ConnectionDetailsList empty() {
        return new ConnectionDetailsList(new ArrayList<ConnectionDetails>());
    }

    private ConnectionDetailsList(List<ConnectionDetails> connectionDetailsList) {
        this.connectionDetailsList = connectionDetailsList;
    }

    public void update(ConnectionDetails newDetails) {
        int indexReplace = -1;
        for (int i = 0; i < connectionDetailsList.size(); i++) {
            if (connectionDetailsList.get(i).getUserID().equals(newDetails.getUserID())) {
                indexReplace = i;
            }

        }

        if (indexReplace != -1) {
            connectionDetailsList.set(indexReplace, newDetails);
        } else {
            connectionDetailsList.add(newDetails);
        }

    }

    public List<ConnectionDetails> getList() {
        return connectionDetailsList;
    }
}

