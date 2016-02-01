package com.kolamomo.network.bio;

import com.kolamomo.network.util.ApiLogger;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 简单的bioClinet
 *
 * @author jay
 */
public class BioClient {
    private static final String DEFAULT_IP = "localhost";
    private static final int DEFAULT_PORT = 8080;
    private Socket socket;

    public BioClient() {
        this(DEFAULT_IP, DEFAULT_PORT);
    }

    /**
     * 创建客户端socket，并与服务端进行连接
     * @param ip
     * @param port
     */
    public BioClient(String ip, int port) {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(InetAddress.getByName(ip), port));
            ApiLogger.info("connect to server:" + socket.getRemoteSocketAddress() );
        } catch (IOException e) {
            ApiLogger.warn("BioClient constructor throws IOException, e: " + e.getMessage());
        }
    }

    /**
     * 向服务端发送请求，并接收服务端回复
     * @param request
     * @return
     */
    public String sendRequest(String request) {
        BufferedReader br = null;
        PrintWriter pw = null;
        String result = null;
        try {
            if(socket == null) {
                System.out.println("socket is null");
            }
            br = getReader(socket);
            pw = getWriter(socket);

            pw.println(request);
            result = br.readLine();
            return result;
        } catch (IOException e) {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e1) {
                    ApiLogger.warn("BioClient sendRequest() throws Exception, e: " + e.getMessage());
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
                    ApiLogger.warn("BioClient sendRequest() throws Exception, e: " + e.getMessage());
                }
                socket = null;
            }
            return result;
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

    public static void main(String[] args) {
        BioClient bioClient = new BioClient();
        try {
            while(true) {
                BufferedReader localBr = new BufferedReader(new InputStreamReader(System.in));
                String request = localBr.readLine();
                String response = bioClient.sendRequest(request);
                System.out.println(response);
            }
        } catch (IOException e) {
            ApiLogger.warn("BioClient main() throws IOException, e: " + e.getMessage());
        }

    }
}
