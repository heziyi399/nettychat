package com.netty.coder;

import com.netty.model.Transportable;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;

/**
 * @author hzy
 * @date 2022-04-02
 */
public class WebMessageEncoder extends MessageToMessageEncoder<Transportable> {
    public WebMessageEncoder()
    {

    }
    @Override
    protected void encode(ChannelHandlerContext ctx, Transportable data, List<Object> out) throws Exception {
        byte[]body = data.getBody();
        ByteBufAllocator allocator = ctx.channel().config().getAllocator();
        ByteBuf buffer = allocator.buffer(body.length+1);
        buffer.writeBytes(new byte[]{data.getType()});
        buffer.writeBytes(body);
        out.add(new BinaryWebSocketFrame(buffer));
    }
}
