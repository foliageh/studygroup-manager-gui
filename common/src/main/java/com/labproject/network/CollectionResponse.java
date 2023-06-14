package com.labproject.network;

import java.io.Serializable;

public class CollectionResponse implements Serializable {
    public final boolean success;
    public String message;
    public Serializable object;

    public CollectionResponse(boolean success, String message, Serializable object) {
        this.success = success;
        this.message = message;
        this.object = object;
    }

    public CollectionResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public CollectionResponse(boolean success, Serializable object) {
        this.success = success;
        this.object = object;
    }
}
