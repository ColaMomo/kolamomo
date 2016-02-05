package com.kolamomo.network.netty.encode.messagepack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * messagePack协议编码器
 */
public class MsgpackEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object obj, ByteBuf byteBuf) throws Exception {
        MessagePack msgpack = new MessagePack();
        byte[] bytes = msgpack.write(obj);
        byteBuf.writeBytes(bytes);
    }
}
