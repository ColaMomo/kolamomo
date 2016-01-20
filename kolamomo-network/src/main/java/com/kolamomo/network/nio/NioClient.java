package com.kolamomo.network.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

import com.kolamomo.network.util.ApiLogger;

public class NioClient {

	private SocketChannel socketChannel = null;
	private Charset charset = Charset.forName("UTF-8");
	private Selector selector;
	private static final String DEFAULT_REMOTE_IP = "localhost";
	private static final int DEFAULT_REMOTE_PORT = 8080;
	
	public NioClient() {
		this(DEFAULT_REMOTE_IP, DEFAULT_REMOTE_PORT);
	}
	
	public NioClient(String remoteIp, int remotePort) {
		try {
			socketChannel = SocketChannel.open();
			InetAddress ia =  InetAddress.getByName(remoteIp);
			InetSocketAddress isa = new InetSocketAddress(ia, remotePort);
			socketChannel.connect(isa);
			socketChannel.configureBlocking(false);
			ApiLogger.info("NioClient-与服务器连接成功");
			selector = Selector.open();
		} catch (IOException e) {
			ApiLogger.error(e.getMessage());
		} 
	}
	
	public void transfer() {
		try {
			socketChannel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
			while(selector.select() > 0) {
				Set<SelectionKey> readyKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = readyKeys.iterator();
				while(it.hasNext()) {
					SelectionKey key = null;
					try {
						key = it.next();
						it.remove();
						
						if(key.isReadable()) {
							receive(key);
						}
						if(key.isWritable()) {
							send(key);
						}
					} catch(IOException e) {
						ApiLogger.error("NioClient.transfer IOException: " + e.getMessage());
						try {
							if(key != null) {
								key.cancel();
								key.channel().close();
							} 
						} catch(IOException ex) {
							ApiLogger.error("NioClient.transfer IOException: " + ex.getMessage());
						}
					}
				}
			}
		} catch(IOException e) {
			ApiLogger.error("NioClient.transfer IOException: " + e.getMessage());
		}
	}
	
	public void send(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel)key.channel();
		synchronized(sendBuffer) {
			sendBuffer.flip();
			socketChannel.write(sendBuffer);
			sendBuffer.compact();
		}
	}
	
	public void receive(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel)key.channel();
		socketChannel.read(receiveBuffer);
		receiveBuffer.flip();
		String recevieData = decode(receiveBuffer);
		
		
	}
	
	private String decode(ByteBuffer buffer) {
		CharBuffer charBuffer = charset.decode(buffer);
		return charBuffer.toString();
	}
	
	private ByteBuffer encode(String str) {
		return charset.encode(str);
	}
	
	public static void main(String args[]) {
		ApiLogger.info("lalala");
	}
}
