package com.netty.coder;

import com.netty.config.ChannelAttr;
import com.netty.model.Pong;
import com.netty.model.SentBody;
import com.netty.model.SentBodyProto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author hzy
 * @date 2022-04-02
 */
//ByteToMessageDecoder:https://blog.csdn.net/linuu/article/details/51351652
public class AppMessageDecoder extends ByteToMessageDecoder {
    //ByteToMessageDecoder的channelRead方法帮我们解决了什么问题？其实就是帮我得到一个完整的数据包，假如我们传递了100个字节，可能接被底层tcp分为三个数据包发送（30，30，40）或者出现粘包情况一次性传递200字节的数据，所以我们需要一个解码器能识别一个完整的数据包即可，而MessageToByteEncoder就是做了这样的工作。
    //
//ByteToMessageDecoder的channelread的逻辑是：如果当前的对象是byteBuf则可以处理，非bytebuf的直接交给下一个handler。
    //从threadlocal获取存放每一次解码之后的结果的对象CodecOutputList，查看我们的bytebuf类型的cumulation是否为空，
    // 若为空代表这是当前这个消息的第一次解码，不为空则代表当前这个数据需要和前几次接受到的数据一起进行解码。

    public AppMessageDecoder() {
    }
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> queue) throws Exception {
        ctx.channel().attr(ChannelAttr.PING_COUNT).set(0);
        if (buffer.readableBytes() >= 3) {
            buffer.markReaderIndex();
            byte type = buffer.readByte();//此方法会让 readerIndex = i + 1;
            byte lv = buffer.readByte();
            byte hv = buffer.readByte();
            int length = this.getContentLength(lv, hv);
            if (buffer.readableBytes() < length) {
                buffer.resetReaderIndex();
            } else {
                byte[] dataBytes = new byte[length];
                buffer.readBytes(dataBytes);
                Object message = this.mappingMessageObject(dataBytes, type);
                queue.add(message);
            }
        }
    }
    public Object mappingMessageObject(byte[] data, byte type) throws Exception {
        if (0 == type) {
            return Pong.getInstance();
        } else {
            SentBodyProto.Model bodyProto = SentBodyProto.Model.parseFrom(data);
            SentBody body = new SentBody();
            body.setKey(bodyProto.getKey());
            body.setTimestamp(bodyProto.getTimestamp());
            body.putAll(bodyProto.getDataMap());
            return body;
        }
    }

    private int getContentLength(byte lv, byte hv) {
        int l = lv & 255;//高位和低位
        //        byte[] header = new byte[]{type, (byte)(length & 255), (byte)(length >> 8 & 255)};
        //        return header;
        int h = hv & 255;
        return l | h << 8;
    }
}
