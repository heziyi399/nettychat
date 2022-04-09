package com.netty.service.impl;

import com.netty.pojo.ChatManager;
import com.netty.mapper.ChatManagerMapper;
import com.netty.service.IChatManagerService;
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
public class ChatManagerServiceImpl extends ServiceImpl<ChatManagerMapper, ChatManager> implements IChatManagerService {

}
