package com.netty.coder;

import com.netty.model.Transportable;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author hzy
 * @date 2022-04-02
 */
public class AppMessageEncoder extends MessageToByteEncoder<Transportable> {
    public AppMessageEncoder() {
    }

    protected void encode(ChannelHandlerContext ctx, Transportable data, ByteBuf out) {
        byte[] body = data.getBody();
        byte[] header = this.createHeader(data.getType(), body.length);
        out.writeBytes(header);
        out.writeBytes(body);
    }

    private byte[] createHeader(byte type, int length) {
        byte[] header = new byte[]{type, (byte)(length & 255), (byte)(length >> 8 & 255)};
        //int = 2byte
        return header;
    }
}
