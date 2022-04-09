package com.netty.config;

/**
 * @author hzy
 * @date 2022-04-02
 */
public interface CIMConstant {
    byte DATA_HEADER_LENGTH = 3;
    byte DATA_TYPE_PONG = 0;
    byte DATA_TYPE_PING = 1;
    byte DATA_TYPE_MESSAGE = 2;
    byte DATA_TYPE_SENT = 3;
    byte DATA_TYPE_REPLY = 4;
    String CLIENT_CONNECT_CLOSED = "client_closed";
}
