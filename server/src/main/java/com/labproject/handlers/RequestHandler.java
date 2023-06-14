package com.labproject.handlers;

import com.labproject.network.CollectionRequest;
import com.labproject.network.CollectionResponse;
import com.labproject.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ForkJoinPool;

public abstract class RequestHandler implements Runnable {
    private final Socket socket;

    public RequestHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            var inStream = new ObjectInputStream(socket.getInputStream());
            var outStream = new ObjectOutputStream(socket.getOutputStream());
            while (true) {
                var request = (CollectionRequest) inStream.readObject();
                var response = createResponse(request);
                ForkJoinPool.commonPool().execute(() -> {
                    try {
                        outStream.writeObject(response);
                        outStream.flush();
                        Server.logger.info("sent a response");
                    } catch (IOException e) {
                        try {
                            outStream.writeObject(new CollectionResponse(false, "msg.internalServerError"));
                            outStream.flush();
                            Server.logger.error("sent a error response: "+e.getMessage());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        } catch (IOException | ClassNotFoundException e) {
            Server.logger.info("client disconnected"); //TODO: more specific error handling
        }
    }

    protected abstract CollectionResponse createResponse(CollectionRequest request);
}