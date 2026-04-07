package com.parent.service.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NettyServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static final Map<String, Channel> USER_CHANNEL_MAP = new ConcurrentHashMap<>();

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        channels.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        channels.remove(ctx.channel());
        USER_CHANNEL_MAP.values().removeIf(c -> c == ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        String msg = frame.text();

        if (msg.startsWith("BIND_USER:")) {
            String userId = msg.substring("BIND_USER:".length());
            USER_CHANNEL_MAP.put(userId, ctx.channel());
            System.out.println("绑定用户：" + userId);
            return;
        }

        for (Channel ch : channels) {
            if (ch != ctx.channel()) {
                ch.writeAndFlush(new TextWebSocketFrame(msg));
            }
        }
    }

    public static void sendToUser(String userId, String message) {
        Channel channel = USER_CHANNEL_MAP.get(userId);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(new TextWebSocketFrame(message));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}