package com.labproject.network;

import java.io.Serializable;
import com.labproject.models.User;

public class CollectionRequest implements Serializable {
    private final String command;
    private Serializable object;
    private User user;

    public CollectionRequest(String command) {
        this.command = command;
    }
    public CollectionRequest(String command, Serializable object) {
        this(command);
        this.object = object;
    }

    public String getCommand() {
        return command;
    }
    public Serializable getObject() {
        return object;
    }
    public User getUser() {
        return user;
    }

    public void setObject(Serializable object) {
        this.object = object;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
