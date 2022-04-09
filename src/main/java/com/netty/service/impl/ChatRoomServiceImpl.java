package com.netty.service.impl;

import com.netty.pojo.ChatRoom;
import com.netty.mapper.ChatRoomMapper;
import com.netty.service.IChatRoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hzy
 * @since 2022-03-30
 */
@Service
public class ChatRoomServiceImpl extends ServiceImpl<ChatRoomMapper, ChatRoom> implements IChatRoomService {

}
