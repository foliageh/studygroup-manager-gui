package com.labproject.client;

import com.labproject.network.CollectionRequest;
import com.labproject.network.CollectionResponse;
import com.labproject.models.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private static Socket socket;
    private static ObjectOutputStream outStream;
    private static ObjectInputStream inStream;
    private static final String HOST = "localhost";
    private static final int PORT;
    static {
        int port = 5454;
        try {
            port = Integer.parseInt(System.getenv().get("SERVER_PORT"));
        } catch (NumberFormatException ignored) {}
        PORT = port;
    }

    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Client.user = user;
    }

    public static void close() {
        try {
            outStream.close();
            inStream.close();
            socket.close();
        } catch (Exception ignored) {}
    }

    public static boolean connectToServer() {
        try {
            socket = new Socket(HOST, PORT);
            outStream = new ObjectOutputStream(socket.getOutputStream());
            inStream = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static CollectionResponse getResponse(CollectionRequest request) {
        request.setUser(user);
        try {
            outStream.writeObject(request);
            outStream.flush();
            return (CollectionResponse) inStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            if (connectToServer()) return getResponse(request);  //TODO: error is not necessarily due to connection failure
            return new CollectionResponse(false, "msg.serverUnavailable");
        }
    }
}