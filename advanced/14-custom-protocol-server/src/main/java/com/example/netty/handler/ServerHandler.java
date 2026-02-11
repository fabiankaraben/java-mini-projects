package com.example.netty.handler;

import com.example.netty.protocol.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) {
        System.out.println("Received: " + msg);
        Message response = null;

        switch (msg.getType()) {
            case Message.PING:
                response = new Message(Message.PONG, new byte[0]);
                break;
            case Message.ECHO:
                response = new Message(Message.ECHO_REPLY, msg.getContent());
                break;
            case Message.REVERSE:
                String original = msg.getContentAsString();
                String reversed = new StringBuilder(original).reverse().toString();
                response = new Message(Message.REVERSE_REPLY, reversed);
                break;
            default:
                System.err.println("Unknown message type: " + msg.getType());
                break;
        }

        if (response != null) {
            ctx.writeAndFlush(response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
