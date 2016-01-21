package com.kolamomo.network.bio;

import com.kolamomo.network.util.ApiLogger;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 简单的BioServer实现
 *
 * 每个请求对应一个处理线程
 *
 * @author jay
 */
public class BioServer {
    private static final int DEFAULT_PORT = 8080;
    private ServerSocket serverSocket;

    public BioServer() {
        this(DEFAULT_PORT);
    }

    public BioServer(int port) {
        try {
            serverSocket = new ServerSocket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(DEFAULT_PORT));
            ApiLogger.info("server start in port: " + port);
        } catch (IOException e) {
            ApiLogger.warn("bioServer start throws IOException, e:" + e.getMessage());
        }
    }

    public void service() {
        Socket socket = null;
        while (true) {
            try {
                socket = serverSocket.accept();
                ApiLogger.info("server accept connection: " + socket.getRemoteSocketAddress().toString());
                BioServerHandler bioServerHandler = new BioServerHandler(socket);
                new Thread(bioServerHandler).start();
            } catch (IOException e) {
                ApiLogger.warn("BioServer service() throws Exception, e: " + e.getMessage());
            }
        }
    }

    private BufferedReader getReader(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    private PrintWriter getWriter(Socket socket) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        return new PrintWriter(outputStream, true);
    }

    class BioServerHandler implements Runnable {
        private Socket socket;

        public BioServerHandler() {}

        public BioServerHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            BufferedReader br = null;
            PrintWriter pw = null;
            try {
                br = getReader(socket);
                pw = getWriter(socket);
                while(true) {
                    String content = br.readLine();
                    ApiLogger.info("BioServerHandler receive data: " + content);
                    pw.println("hello: " + content);
                    if(content == null || "bye".equals(content)) {
                        break;
                    }
                }
            } catch (IOException e) {
                if(br != null) {
                    try {
                        br.close();
                    } catch (IOException e1) {
                        ApiLogger.warn("BioServerHandler run() throws Exception, e: " + e.getMessage());
                    }
                }
                if(pw != null) {
                    pw.close();
                    pw = null;
                }
                if(socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        ApiLogger.warn("BioServerHandler run() throws Exception, e: " + e.getMessage());
                    }
                    socket = null;
                }
            }
        }
    }

    public static void main(String[] args) {
        BioServer bioServer = new BioServer();
        bioServer.service();
    }
}
