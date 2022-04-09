package com.netty.handler;

import com.netty.model.SentBody;
import io.netty.channel.Channel;

/**
 * @author hzy
 * @date 2022-04-02
 */
public interface CIMRequestHandler {
    void process(Channel var1, SentBody var2);
}
