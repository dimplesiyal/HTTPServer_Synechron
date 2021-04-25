package com.org.requestHandler;

import com.org.fileHandler.FileManagement;
import com.org.responseHandler.ReponseHandler;

import java.io.*;
import java.net.Socket;
import java.net.URLConnection;
import java.util.StringTokenizer;

public class HttpRequestHandler implements Runnable {

    final static String CRLF = "\r\n";
    Socket socket;
    String writeToFile;
    FileManagement fileManagement = new FileManagement();
    ReponseHandler reponseHandler = new ReponseHandler();

    public HttpRequestHandler(Socket socket, String dirName) {
        this.socket = socket;
        String directoryPath = "C:/"+dirName;
        this.writeToFile = new StringBuilder().append(directoryPath).append(fileManagement.createRandomFileName()).toString();
        fileManagement.createDirectories(directoryPath);
    }

    public void run() {
        try {
            processRequest(this);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private String contentType(String filename) {
        return URLConnection.guessContentTypeFromName(filename);
    }


    private void processRequest(HttpRequestHandler httpRequestHandler) throws Exception {

        InputStream inputStream = httpRequestHandler.socket.getInputStream();
        DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(this.writeToFile));

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));

        String requestLine = bufferedReader.readLine();

        System.out.println();
        System.out.println(requestLine);

        String headerLine = null;
        while ((headerLine = bufferedReader.readLine()).length() != 0) {
            System.out.println(headerLine);
        }


        StringTokenizer tokens = new StringTokenizer(requestLine);
        if (requestLine.contains("POST")) {
            tokens.nextToken();
            String fileName = tokens.nextToken();

            fileName = new StringBuilder().append("C:/").append(fileName).toString();

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
                fileManagement.writeToFile(fileInputStream, outputStream);
                fileInputStream.close();
            } else {
                outputStream.writeBytes(CRLF);
            }

            reponseHandler.sendSuccessResponse(socket);

        } else
        {
            reponseHandler.sendErrorResponse(socket);
        }
        outputStream.close();
        bufferedReader.close();
    }

}
