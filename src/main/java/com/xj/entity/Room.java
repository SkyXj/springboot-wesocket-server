package com.xj.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("room")
public class Room implements Serializable {
    @TableId(type= IdType.AUTO)
    private Integer id;
    @TableField(value="name")
    private String name;

    @TableField(value="isFull")
    private Integer isFull;

    @TableField(exist = false)
    private List<User> users;

    @TableField(exist = false)
    private List<Integer> pais;

    //最新出的牌
    @TableField(exist = false)
    private Integer newpai;

    //最新出牌的人
    @TableField(exist = false)
    private String sid;

    @TableField(exist = false)
    private boolean isStart ;

    @TableField(exist = false)
    private Integer qiaoBao;

    //庄的id
    @TableField(exist = false)
    private String zhuangid;

    //播放声音提示所有人
    @TableField(exist = false)
    private String voiceType;

}
