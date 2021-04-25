package com.org.responseHandler;

import java.io.IOException;
import java.net.Socket;

public class ReponseHandler {

    public static void sendSuccessResponse(Socket client) throws IOException {
        String httpResponse = "HTTP/1.1 200 OK\r\n\r\n";
        client.getOutputStream().write(httpResponse.getBytes("UTF-8"));
        client.close();
    }

    public static void sendErrorResponse(Socket client) throws IOException {
        String httpResponse = "HTTP/1.1 405 Method not allowed.\r\n\r\n";
        client.getOutputStream().write(httpResponse.getBytes("UTF-8"));
        client.close();
    }
}
