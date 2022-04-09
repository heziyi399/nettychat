package com.netty.group;

import com.netty.config.ChannelAttr;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

/**
 * @author hzy
 * @date 2022-04-02
 */
@Component
public class TagSessionGroup extends SessionGroup {
    public TagSessionGroup() {
    }

    protected String getKey(Channel channel) {
        return (String)channel.attr(ChannelAttr.TAG).get();
    }
}
