package com.example.netty.protocol;

import java.nio.charset.StandardCharsets;

public class Message {
    public static final byte PING = 0x01;
    public static final byte PONG = 0x02;
    public static final byte ECHO = 0x03;
    public static final byte ECHO_REPLY = 0x04;
    public static final byte REVERSE = 0x05;
    public static final byte REVERSE_REPLY = 0x06;

    private byte type;
    private byte[] content;

    public Message(byte type, byte[] content) {
        this.type = type;
        this.content = content;
    }

    public Message(byte type, String content) {
        this.type = type;
        this.content = content != null ? content.getBytes(StandardCharsets.UTF_8) : new byte[0];
    }

    public byte getType() {
        return type;
    }

    public byte[] getContent() {
        return content;
    }

    public String getContentAsString() {
        return new String(content, StandardCharsets.UTF_8);
    }

    @Override
    public String toString() {
        return "Message{type=" + type + ", content='" + getContentAsString() + "'}";
    }
}
