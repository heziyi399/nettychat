package com.netty.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.netty.group.GroupMessagePusher;
import com.netty.mapper.ChatRoomMapper;
import com.netty.mapper.ChatRoomMemberMapper;
import com.netty.model.MessageBody;
import com.netty.pojo.ChatRoomMember;
import com.netty.mapper.ChatRoomMemberMapper;
import com.netty.pojo.ChatRoomMember;
import com.netty.service.IChatRoomMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.netty.service.IChatRoomService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hzy
 * @since 2022-03-30
 */
@Service
public class ChatRoomMemberServiceImpl extends
        ServiceImpl<ChatRoomMemberMapper, ChatRoomMember> implements IChatRoomMemberService {
    @Resource
    private GroupMessagePusher groupMessagePusher;

    @Resource
    private ChatRoomMemberMapper memberRepository;

}
