package com.netty.mapper;

import com.netty.pojo.ChatManager;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hzy
 * @since 2022-03-30
 */
@Component
public interface ChatManagerMapper extends BaseMapper<ChatManager> {

}
