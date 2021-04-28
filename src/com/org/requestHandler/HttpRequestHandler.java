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
    private String dirName;

    public HttpRequestHandler(Socket socket, String directoryName) {
        this.socket = socket;
        this.dirName = directoryName;
    }

    public void run() {
            processRequest(this);
    }

    private String contentType(String filename) {
        return URLConnection.guessContentTypeFromName(filename);
    }


    private void processRequest(HttpRequestHandler httpRequestHandler) {

        try {
            InputStream inputStream = httpRequestHandler.socket.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String requestLine = bufferedReader.readLine();

            System.out.println();
            System.out.println(requestLine);

            StringTokenizer tokens = new StringTokenizer(requestLine);
            if (requestLine.contains("POST")) {
                tokens.nextToken();
                String fileName = tokens.nextToken();

                String writeToFile = new StringBuilder().append(this.dirName).append(fileName).toString();
                DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(writeToFile));

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
                    FileManagement.writeToFile(fileInputStream, outputStream);
                    fileInputStream.close();
                } else {
                    outputStream.writeBytes(CRLF);
                }

                ResponseHandler.sendSuccessResponse(socket.getOutputStream());
                outputStream.close();

            } else {
                ResponseHandler.sendErrorResponse(socket.getOutputStream());
            }

            bufferedReader.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
