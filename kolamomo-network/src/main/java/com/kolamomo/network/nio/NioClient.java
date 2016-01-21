package com.kolamomo.network.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import com.kolamomo.network.util.ApiLogger;

/**
 * NioClient
 */
public class NioClient {
    private static final String DEFAULT_REMOTE_IP = "localhost";
    private static final int DEFAULT_REMOTE_PORT = 8080;
    private static final int DEFAULT_BUFFER_SIZE = 1024;

    private SocketChannel socketChannel;
	private Selector selector;

    public NioClient() {
        this(DEFAULT_REMOTE_IP, DEFAULT_REMOTE_PORT);
    }

	public NioClient(String remoteIp, int remotePort) {
		try {
			socketChannel = SocketChannel.open();
			selector = Selector.open();
            socketChannel.configureBlocking(false);
            boolean connectResult = socketChannel.connect(new InetSocketAddress(InetAddress.getByName(remoteIp), remotePort));

            //configureBlocking(false) 放在connect之后或不配置，client将以阻塞方式进行连接，
            //这时候将阻塞直到完成tcp三次握手，此时需要将SelectionKey设置为OP_WRITE向服务端发送数据
            if(connectResult) {
                ApiLogger.info("finish connection in block mode, remote ip: " + socketChannel.socket().getRemoteSocketAddress());
                socketChannel.register(selector, SelectionKey.OP_WRITE);
            }
            //configureBlocking(false) 放在connect之前, client将以非阻塞的方式进行连接，
            //这时候并未完成tcp三次握手，需要将SelectionKey设置为OP_CONNECT以完成连接
            else {
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
            }
		} catch (IOException e) {
			ApiLogger.error(e.getMessage());
		} 
	}
	
	public void transfer() {
        SelectionKey selectionKey = null;
        try {

            while(selector.select() > 0) {
				Set<SelectionKey> readyKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = readyKeys.iterator();
                try {
                    while (it.hasNext()) {
                        selectionKey = it.next();
                        it.remove();

                        if (selectionKey.isConnectable()) {
                            connect(selectionKey);
                        }
                        if (selectionKey.isReadable()) {
                            receive(selectionKey);
                        }
                        if (selectionKey.isWritable()) {
                            send(selectionKey);
                        }
                    }
                } catch(IOException e) {
                    ApiLogger.error("NioClient transfer() throws IOException: " + e.getMessage());
                    try {
                        if(selectionKey != null) {
                            selectionKey.cancel();
                            selectionKey.channel().close();
                        }
                    } catch(IOException ex) {
                        ApiLogger.error("NioClient transfer() throws IOException: " + ex.getMessage());
                    }
                }
			}
		} catch(Exception e) {
            ApiLogger.error("NioClient transfer() throws IOException: " + e.getMessage());
        }
	}

    /**
     * 完成三次握手连接
     */
    private void connect(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        socketChannel = (SocketChannel) selectionKey.channel();
        if (socketChannel.isConnectionPending()) {
            socketChannel.finishConnect();
            ApiLogger.info("finish connection in non-block mode, remote ip: " + socketChannel.socket().getRemoteSocketAddress());
        }
        socketChannel.register(selector, SelectionKey.OP_WRITE);
    }

    /**
     * 发送请求
     */
    private void send(SelectionKey selectionKey) throws IOException, InterruptedException {
        Thread.sleep(1000);
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        String request = new Date(System.currentTimeMillis()).toString();

        ByteBuffer writeBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
        writeBuffer.put(request.getBytes());
        writeBuffer.flip();
        while(writeBuffer.hasRemaining()) {
            socketChannel.write(writeBuffer);
        }
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    /**
     * 接收数据
     */
    private void receive(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer readBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
        socketChannel.read(readBuffer);
        readBuffer.flip();
        byte[] bytes = new byte[readBuffer.remaining()];
        readBuffer.get(bytes);
        String body = new String(bytes, "UTF-8");
        ApiLogger.info("receive data: " + body);
        socketChannel.register(selector, SelectionKey.OP_WRITE);
    }

    public static void main(String args[]) {
		NioClient nioClient = new NioClient();
        nioClient.transfer();
	}

}
