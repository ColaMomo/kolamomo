package com.kolamomo.io.nio.buffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by jiangchao on 16/1/31.
 */
public class NioBuffer {
    private static final String FILE_NAME = "/home/jay/project/kolamomo/kolamomo/kolamomo-io/target/classes/com/kolamomo/io/nio/buffer/Hello.class";
    private static final int DEFAULT_BUFFER_SIZE = 48;
    private static final int DEFAULT_BYTEARRAY_SIZE = 8;
    public void readClassFile() {
        try {
            RandomAccessFile file = new RandomAccessFile(FILE_NAME, "rw");
            FileChannel fileChannel = file.getChannel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
            int bytesRead = 0;
            while((bytesRead = fileChannel.read(byteBuffer)) != -1) {
                byteBuffer.flip();
                while(byteBuffer.remaining() > 0) {
                    byte[] bytes = new byte[Math.min(byteBuffer.remaining(), DEFAULT_BYTEARRAY_SIZE)];

                    byteBuffer.get(bytes);
                    for(byte b : bytes) {
                        System.out.print(byteToHexString(b));
                    }
                    System.out.print("    ");
                    for(byte b : bytes) {
                        System.out.print(byteToChar(b));
                    }
                    System.out.println();

                }
                byteBuffer.clear();
            }
            file.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {

        }

    }

    //byte转16进制字符串
    private String byteToHexString(byte b) {
        String result = "";
        int v = b & 0xFF;
        String hv = Integer.toHexString(v);
        if (hv.length() < 2) {
            result += 0;
        }
        result += hv;
        return result;

    }

    //byte转char, 只转换可打印字符，不可打印字符输出'.'
    public static char byteToChar(byte b) {
        if(b >= 32 && b <= 126) {
            return (char)(b);
        }
        return '.';
    }



    public static void main(String[] args) {
        NioBuffer nioBuffer = new NioBuffer();
        nioBuffer.readClassFile();
    }
}
