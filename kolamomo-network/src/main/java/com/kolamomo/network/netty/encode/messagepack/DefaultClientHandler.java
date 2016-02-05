package com.kolamomo.network.netty.encode.messagepack;

import com.kolamomo.network.util.ApiLogger;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jay on 16-1-29.
 */
public class DefaultClientHandler extends ChannelHandlerAdapter {
    private int num;
    private static final int DEFAULT_NUM = 100;

    public DefaultClientHandler() {
        this(DEFAULT_NUM);
    }

    public DefaultClientHandler(int num) {
        this.num = num;
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        List<User> userList = getUser();
        for(User user : userList) {
            context.write(user);
        }
        context.flush();
    }

    private List<User> getUser() {
        List<User> userList = new ArrayList<User>();
        for(int i = 0; i < num; i++) {
            User user =  new User();
            user.setId(i);
            user.setName("lalala" + i);
            userList.add(user);
        }
        return userList;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) throws Exception {
        ApiLogger.info("server response: " + message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        ApiLogger.warn("DefaultClientHandler exception: " + cause.getMessage());
        context.close();
    }
}
