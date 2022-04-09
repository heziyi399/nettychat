package com.netty.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author hzy
 * @since 2022-03-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_chat_manager")
public class ChatManager implements Serializable {

    private static final long serialVersionUID = 1L;

    private String account;

    private String name;

    private String password;

    private String state;


}
