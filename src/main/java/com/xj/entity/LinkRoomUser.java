package com.xj.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

@TableName("link_room_user")
@Data
public class LinkRoomUser {
    @TableId(type= IdType.AUTO)
    private Integer id;
    @TableField("userid")
    private Integer userid;
    @TableField("roomid")
    private Integer roomid;
}
