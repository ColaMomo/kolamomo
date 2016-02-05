package com.kolamomo.network.netty.encode.messagepack;

import com.kolamomo.network.util.ApiLogger;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * Created by jay on 16-1-22.
 */
public class DefaultServerHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext context, Object message) throws Exception {
        ApiLogger.info("server receive: " + message);
        context.write(message);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context) throws Exception {
        context.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        ApiLogger.warn("DefaultServerHandler exception: " + cause.getMessage());
        context.close();
    }
}
