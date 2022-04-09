package com.netty.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("t_chat_room_")
public class ChatRoomMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Date createTime;

    private String name;

    private Long roomId;

    private String uid;


}
