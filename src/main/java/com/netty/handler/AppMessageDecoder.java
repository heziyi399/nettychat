package com.netty.handler;

import com.netty.model.Pong;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author hzy
 * @date 2022-03-30
 */
public class AppMessageDecoder extends ByteToMessageDecoder {
//    2.ByteToMessageDecoder是netty解码的一个抽象类，其实现了channelRead方法，而实现者只要实现其decode方法即可。
//
//            3.那ByteToMessageDecoder的channelRead方法帮我们解决了什么问题？
//    其实就是帮我得到一个完整的数据包，假如我们传递了100个字节，可能接被底层tcp分为三个数据包发送（30，30，40）
//    或者出现粘包情况一次性传递200字节的数据，所以我们需要一个解码器能识别一个完整的数据包即可，
//    而MessageToByteEncoder就是做了这样的工作。
public AppMessageDecoder() {
}
@Override
    protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> queue) throws Exception {

    }


    private int getContentLength(byte lv, byte hv) {
        int l = lv & 255;
        int h = hv & 255;
        return l | h << 8;
    }


}
