package com.netty.handler;

import com.netty.config.ChannelAttr;
import com.netty.config.NettyHandler;
import com.netty.group.TagSessionGroup;
import com.netty.model.ReplyBody;
import com.netty.model.SentBody;
import com.netty.pojo.ChatRoomMember;
import com.netty.service.IChatRoomMemberService;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.springframework.http.HttpStatus;

import javax.annotation.Resource;

/**
 * @author hzy
 * @date 2022-04-02
 */
@NettyHandler(key="client_bind")

public class BindHandler implements CIMRequestHandler {

    @Resource
    private TagSessionGroup tagsessionGroup;

    @Resource
    private IChatRoomMemberService ChatRoomMemberService;

    @Override
    public void process(Channel channel, SentBody body) {

        ReplyBody reply = new ReplyBody();
        reply.setKey(body.getKey());
        reply.setCode(HttpStatus.OK.value());
        reply.setTimestamp(System.currentTimeMillis());

        String uid = body.get("uid");

        String name = body.get("name");

        long roomId = body.getLong("roomId");

        ChatRoomMember ChatRoomMember = new ChatRoomMember();
        ChatRoomMember.setUid(uid);
        ChatRoomMember.setName(name);
        ChatRoomMember.setRoomId(roomId);
    //    ChatRoomMemberService.add(ChatRoomMember);

        channel.attr(ChannelAttr.UID).set(uid);

        channel.attr(AttributeKey.valueOf("name")).set(name);

        channel.attr(ChannelAttr.TAG).set(String.valueOf(roomId));

        tagsessionGroup.add(channel);

        channel.writeAndFlush(reply);
    }
}
