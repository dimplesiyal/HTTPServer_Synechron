package com.org.requestHandler;

import com.org.fileHandler.FileManagement;
import com.org.responseHandler.ResponseHandler;

import java.io.*;
import java.net.Socket;
import java.net.URLConnection;
import java.util.StringTokenizer;

public class HttpRequestHandler implements Runnable {

    private final static String CRLF = "\r\n";
    private Socket socket;

    public HttpRequestHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {

        processRequest(this);

    }

    private String contentType(String filename) {
        return URLConnection.guessContentTypeFromName(filename);
    }


    private void processRequest(HttpRequestHandler httpRequestHandler) {

        DataOutputStream outputStream = null;
       // BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = this.socket.getInputStream();
            DataOutputStream os = new DataOutputStream(new FileOutputStream(new StringBuilder().append("C:/").append("/tmp").append("//filename").toString()));

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String requestLine = bufferedReader.readLine();

            System.out.println();
            System.out.println(requestLine);

            StringTokenizer tokens = new StringTokenizer(requestLine);
            if (requestLine.contains("POST")) {
                tokens.nextToken();
                String fileName = tokens.nextToken();

                fileName = new StringBuilder().append("C:/").append(fileName).toString();
                outputStream = new DataOutputStream(new FileOutputStream(fileName));

                FileInputStream fileInputStream = null;
                boolean fileExists = true;
                try {
                    fileInputStream = new FileInputStream(fileName);
                } catch (FileNotFoundException e) {
                    fileExists = false;
                }

                String statusLine = null;
                String contentTypeLine = null;
                String entityBody = null;
                if (fileExists) {
                    statusLine = "200 OK";
                    contentTypeLine = "Content-type: " +
                            httpRequestHandler.contentType(fileName) + CRLF;
                } else {
                    statusLine = "404 Not Found";
                    contentTypeLine = "Content-type";
                    entityBody = "<HTML>" +
                            "<HEAD><TITLE>Not Found</TITLE></HEAD>" +
                            "<BODY>Not Found</BODY></HTML>";
                }

                if (fileExists) {
                    FileManagement.writeToFile(fileInputStream, outputStream);
                    fileInputStream.close();
                } else {
                    outputStream.writeBytes(CRLF);
                }

                ResponseHandler.sendSuccessResponse(socket.getOutputStream());

            } else {
                ResponseHandler.sendErrorResponse(socket.getOutputStream());
            }
            outputStream.close();
            bufferedReader.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
