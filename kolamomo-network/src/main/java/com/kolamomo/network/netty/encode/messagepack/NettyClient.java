package com.kolamomo.network.netty.encode.messagepack;

import com.kolamomo.network.util.ApiLogger;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * Created by jay on 16-1-29.
 */
public class NettyClient {
    private static final String DEFAULT_IP = "localhost";
    private static final int DEFAULT_PORT = 8080;

    public NettyClient() {
        this(DEFAULT_IP, DEFAULT_PORT);
    }

    public NettyClient(String host, int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChildChannelHandler());

            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            ApiLogger.warn("NettyClient constructor exception, e:" + e.getMessage());
        } finally {
            group.shutdownGracefully();
        }
    }

    class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast("frame decoder", new LengthFieldBasedFrameDecoder(65536, 0, 2, 0, 2));
            socketChannel.pipeline().addLast("msgpack decoder", new MsgpackDecoder());
            socketChannel.pipeline().addLast("frame encoder", new LengthFieldPrepender(2));
            socketChannel.pipeline().addLast("msgpack encoder", new MsgpackEncoder());
            socketChannel.pipeline().addLast(new DefaultClientHandler());
        }
    }

    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient();
    }
}
