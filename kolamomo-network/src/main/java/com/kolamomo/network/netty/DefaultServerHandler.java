package com.kolamomo.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by jay on 16-1-22.
 */
public class DefaultServerHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext context, Object message) {
        ByteBuf buf = (ByteBuf) message;

    }

    @Override
    public void channelWrite()
}
