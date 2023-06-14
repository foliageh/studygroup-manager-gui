package com.labproject.server;

import com.labproject.handlers.CollectionRequestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class Server {
    public static final Logger logger = LogManager.getLogger(Server.class);
    private static final String HOST = "localhost";
    private static final int PORT;
    static {
        int port = 5454;
        try {
            port = Integer.parseInt(System.getenv().get("SERVER_PORT"));
        } catch (NumberFormatException e) {
            logger.debug("Port variable SERVER_PORT isn't set or invalid, using default port "+port);
        }
        PORT = port;
    }

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(HOST, PORT));
        } catch (BindException e) {
            logger.debug("Socket address already in use.");
            System.exit(1);
        } catch (IOException e) {
            logger.fatal("Error opening socket.");
        }
        logger.info("server launched");

        var executorService = Executors.newCachedThreadPool();

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                logger.info("new client connected");
                executorService.submit(new CollectionRequestHandler(clientSocket));
            } catch (IOException e) {
                logger.info("failed to connect to the client");
            }
        }
    }
}
