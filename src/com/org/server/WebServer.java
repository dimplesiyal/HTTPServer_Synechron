package com.org.server;

import com.org.constants.ApplicationConstants;
import com.org.exceptionHandler.InvalidArgumentException;
import com.org.requestHandler.HttpRequestHandler;
import com.org.utils.ValidationUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {


    public static void main(String[] args) throws InvalidArgumentException {

        int argumentsLength = args.length;
        int maxPoolSize = 3;
        ExecutorService pool = null;
        try {
            if (argumentsLength == 2) {
                int portNumber = Integer.parseInt(args[0]);
                String directoryName = args[1];
                if (!ValidationUtils.validatePortAndDirectory(portNumber, directoryName)) {
                    throw new InvalidArgumentException(ApplicationConstants.INVALID_ARGUMENT_EXCEPTION);
                }

                pool = Executors.newFixedThreadPool(maxPoolSize);
                try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
                    System.out.println("Listening for connection on port" + portNumber + "  ....");
                    while (true) {

                        Socket socket = serverSocket.accept();
                        HttpRequestHandler httpRequestHandler = new HttpRequestHandler(socket, directoryName);
                        pool.execute(httpRequestHandler);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }
}
