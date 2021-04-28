package com.org.responseHandler;

import java.io.IOException;
import java.io.OutputStream;

public class ResponseHandler {

    public static void sendSuccessResponse(OutputStream outputStream) throws IOException {
        String httpResponse = "HTTP/1.1 200 OK\r\n\r\n";
        outputStream.write(httpResponse.getBytes("UTF-8"));
    }

    public static void sendErrorResponse(OutputStream outputStream) throws IOException {
        String httpResponse = "HTTP/1.1 405 Method not allowed.\r\n\r\n";
        outputStream.write(httpResponse.getBytes("UTF-8"));
    }
}
