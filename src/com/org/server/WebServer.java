package com.org.server;

import com.org.requestHandler.HttpRequestHandler;
import com.sun.deploy.net.HttpRequest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {

    public static void main(String[] args) {

        int argumentsLength = args.length;

        if (argumentsLength != 0) {
            int portNumber = Integer.parseInt(args[0]);
            String DirectoryName = args[1];
            int maxPoolSize = 3;
            ExecutorService pool = Executors.newFixedThreadPool(maxPoolSize);

            try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
                System.out.println("Listening for connection on port" + portNumber + "  ....");
                while (true) {
                    Socket socket = serverSocket.accept();
                    HttpRequestHandler httpRequestHandler = new HttpRequestHandler(socket, DirectoryName);
                    pool.execute(httpRequestHandler);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                pool.shutdown();
            }
        }
    }
}
