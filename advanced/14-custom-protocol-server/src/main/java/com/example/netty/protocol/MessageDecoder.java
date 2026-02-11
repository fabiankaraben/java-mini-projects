package com.example.netty.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        // Wait until the type (1 byte) and length (4 bytes) are available
        if (in.readableBytes() < 5) {
            return;
        }

        in.markReaderIndex();
        
        byte type = in.readByte();
        int length = in.readInt();

        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        byte[] content = new byte[length];
        in.readBytes(content);

        out.add(new Message(type, content));
    }
}
