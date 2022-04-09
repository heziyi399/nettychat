package com.netty.group;

import com.netty.config.ChannelAttr;
import com.netty.model.MessageBody;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author hzy
 * @date 2022-04-02
 */
/**
 * 存储每个客户端接入进来时的 channel 对象
 * 主要用于使用 writeAndFlush 方法广播信息
 */
public class SessionGroup extends ConcurrentHashMap<String, Collection<Channel>> {
    //map： /**
    //     * 用于客户端和服务端握手时存储用户id和netty Channel对应关系
    //     */
    private static final Collection<Channel> EMPTY_LIST = new LinkedList();
    private final transient ChannelFutureListener remover = new ChannelFutureListener() {
        public void operationComplete(ChannelFuture future) {
            future.removeListener(this);
            SessionGroup.this.remove(future.channel());
        }
    };
    public SessionGroup() {
    }

    protected String getKey(Channel channel) {
        return (String)channel.attr(ChannelAttr.UID).get();
    }
    public void remove(Channel channel) {
        String uid = this.getKey(channel);
        if (uid != null) {
            Collection<Channel> collections = (Collection)this.getOrDefault(uid, EMPTY_LIST);
            collections.remove(channel);
            if (collections.isEmpty()) {
                this.remove(uid);
            }

        }
    }

    public void add(Channel channel) {
        String uid = this.getKey(channel);
        if (uid != null && channel.isActive()) {
            channel.closeFuture().addListener(this.remover);
            Collection<Channel> collections = (Collection)this.putIfAbsent(uid, new ConcurrentLinkedQueue(Collections.singleton(channel)));
            if (collections != null) {
                collections.add(channel);
            }

            if (!channel.isActive()) {
                this.remove(channel);
            }

        }
    }

    public void write(String key, MessageBody message) {
        this.find(key).forEach((channel) -> {
            channel.writeAndFlush(message);
        });
    }

    public void write(String key, MessageBody message, Predicate<Channel> matcher) {
        this.find(key).stream().filter(matcher).forEach((channel) -> {
            channel.writeAndFlush(message);
        });
    }

    public void write(String key, MessageBody message, Collection<String> excludedSet) {
        this.find(key).stream().filter((channel) -> {
            return excludedSet == null || !excludedSet.contains(channel.attr(ChannelAttr.UID).get());
        }).forEach((channel) -> {
            channel.writeAndFlush(message);
        });
    }

    public void write(MessageBody message) {
        this.write(message.getReceiver(), message);
    }

    public Collection<Channel> find(String key) {
        return (Collection)this.getOrDefault(key, EMPTY_LIST);
    }

    public Collection<Channel> find(String key, String... channel) {
        List<String> channels = Arrays.asList(channel);
        return (Collection)this.find(key).stream().filter((item) -> {
            return channels.contains(item.attr(ChannelAttr.CHANNEL).get());
        }).collect(Collectors.toList());
    }
}
