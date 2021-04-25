package com.org.requestHandler;

import java.io.*;
import java.net.Socket;
import java.net.URLConnection;
import java.util.StringTokenizer;

public class HttpRequestHandler implements Runnable {

    final static String CRLF = "\r\n";
    Socket socket;
    String writeToFile;

    public HttpRequestHandler(Socket socket, String dirName) {
        this.socket = socket;
        String filename = "//Write_" + socket.getLocalPort() + ".txt";
        this.writeToFile = new StringBuilder().append("C:/").append(dirName).append(filename).toString();
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

    private static void sendBytes(FileInputStream fis, OutputStream os)
            throws Exception {
        try {
            int n;
            while ((n = fis.read()) != -1) {
                os.write(n);
            }
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (os != null) {
                os.close();
            }
        }
        System.out.println("File Copied");

    }

    private void processRequest(HttpRequestHandler httpRequestHandler) throws Exception {

        InputStream is = httpRequestHandler.socket.getInputStream();
        DataOutputStream os = new DataOutputStream(new FileOutputStream(this.writeToFile));

        BufferedReader br = new BufferedReader(
                new InputStreamReader(is));

        String requestLine = br.readLine();

        System.out.println();
        System.out.println(requestLine);

        String headerLine = null;
        while ((headerLine = br.readLine()).length() != 0) {
            System.out.println(headerLine);
        }


        StringTokenizer tokens = new StringTokenizer(requestLine);
        if (requestLine.contains("POST")) {
            tokens.nextToken();
            String fileName = tokens.nextToken();

            fileName = new StringBuilder().append("C:/").append(fileName).toString();

            FileInputStream fis = null;
            boolean fileExists = true;
            try {
                fis = new FileInputStream(fileName);
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
                sendBytes(fis, os);
                fis.close();
            } else {
                os.writeBytes(CRLF);
            }

            sendSuccessResponse(socket);

        } else
        {
            sendErrorResponse(socket);
        }
        os.close();
        br.close();
    }

    private static void sendSuccessResponse(Socket client) throws IOException {
        String httpResponse = "HTTP/1.1 200 OK\r\n\r\n";
        client.getOutputStream().write(httpResponse.getBytes("UTF-8"));
        client.close();
    }

    private static void sendErrorResponse(Socket client) throws IOException {
        String httpResponse = "HTTP/1.1 405 Method not allowed.\r\n\r\n";
        client.getOutputStream().write(httpResponse.getBytes("UTF-8"));
        client.close();
    }

}
