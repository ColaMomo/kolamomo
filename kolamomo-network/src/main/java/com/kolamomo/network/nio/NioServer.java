package com.kolamomo.network.nio;

import com.kolamomo.network.util.ApiLogger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NioServer
 *
 */
public class NioServer {
    private static final int DEFAULT_PORT = 8080;
    private static final int DEFAULT_BUFFER_SIZE = 1024;

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public NioServer() {
        this(DEFAULT_PORT);
    }

    public NioServer(int port) {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().setReuseAddress(true);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            ApiLogger.info("server start in port" + port);
        } catch (IOException e) {
            ApiLogger.warn("NioServer constructor throws IOException, e: " + e.getMessage());
        }
    }

    public void service() {
        SelectionKey selectionKey = null;
        try {
            while (selector.select() > 0) {
                Set<SelectionKey> readyKeys = selector.selectedKeys();
                Iterator it = readyKeys.iterator();
                try {
                    while (it.hasNext()) {
                        selectionKey = (SelectionKey) it.next();
                        it.remove();

                        if (selectionKey.isAcceptable()) {
                            accept(selectionKey);
                        }
                        if (selectionKey.isReadable()) {
                            receive(selectionKey);
                        }
                        if (selectionKey.isWritable()) {
                            send(selectionKey);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (selectionKey != null) {
                        selectionKey.cancel();
                        try {
                            selectionKey.channel().close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 接收连接
     */
    private void accept(SelectionKey selectionKey) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        ApiLogger.info("NioServer accept connection: " + socketChannel.socket().getRemoteSocketAddress());
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    /**
     * 接收数据
     */
    private void receive(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        //将数据从socketChannel读取到readBuffer中
        ByteBuffer readBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
        socketChannel.read(readBuffer);
        readBuffer.flip();

        //将readBuffer中的数据转换为字符串
        byte[] bytes = new byte[readBuffer.remaining()];
        readBuffer.get(bytes);
        String body = new String(bytes, "UTF-8");
        ApiLogger.info("receive data: " + body);

        //准备回复数据，将数据放到selector的attachment中
        String response = "hello: " + body;
        socketChannel.register(selector, SelectionKey.OP_WRITE, response);
    }

    /**
     * 发送数据
     */
    private void send(SelectionKey selectionKey) throws IOException {
        //从attachment中取出数据
        String response = (String) selectionKey.attachment();
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        //将字符串数据写入buffer中
        ByteBuffer writeBuffer = ByteBuffer.allocate(response.length());
        writeBuffer.put(response.getBytes());
        writeBuffer.flip();

        //发送buffer中的数据
        while(writeBuffer.hasRemaining()) {
            socketChannel.write(writeBuffer);
        }
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    public static void main(String[] args) {
        NioServer nioServer = new NioServer();
        nioServer.service();
    }
}
