package com.netty.coder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;

/**
 * @author hzy
 * @date 2022-04-02
 */
public class WebMessageDecoder extends MessageToMessageDecoder<BinaryWebSocketFrame> {
    @Override
    protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame msg, List<Object> out) throws Exception {

    }
}
