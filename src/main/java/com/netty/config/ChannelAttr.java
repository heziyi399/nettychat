package com.netty.config;

import io.netty.util.AttributeKey;

/**
 * @author hzy
 * @date 2022-03-30
 */
public interface ChannelAttr {
    //1)AttributeMap这是是绑定在Channel或者ChannelHandlerContext上的一个附件，相当于依附在这两个对象上的寄生虫一样，相当于附件一样，
    AttributeKey<Integer> PING_COUNT = AttributeKey.valueOf("ping_count");
    AttributeKey<String> UID = AttributeKey.valueOf("uid");
    AttributeKey<String> CHANNEL = AttributeKey.valueOf("channel");
    AttributeKey<String> ID = AttributeKey.valueOf("id");
    AttributeKey<String> DEVICE_ID = AttributeKey.valueOf("device_id");
    AttributeKey<String> TAG = AttributeKey.valueOf("tag");
    AttributeKey<String> LANGUAGE = AttributeKey.valueOf("language");
//    每一个ChannelHandlerContext都有属于自己的上下文，
//    也就说每一个ChannelHandlerContext上如果有AttributeMap都是绑定上下文的，
//    也就说如果A的ChannelHandlerContext中的AttributeMap，B的ChannelHandlerContext是无法读取到的
//
//    但是Channel上的AttributeMap就是大家共享的，每一个ChannelHandler都能获取到

}
