package com.xj.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.xj.controller.WebSocketServer;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("user")
public class User implements Serializable {

    @TableId(type=IdType.AUTO)
    private Integer id;
    @TableField(value="name")
    private String name;
    @TableField(value="password")
    private String password;
    @TableField(value="nickname")
    private String nickname;
    @TableField(value="img")
    private String img;

    @TableField(exist = false)
    private boolean isMaster;

    @TableField(exist = false)
    private boolean isCanChuPai;

    @TableField(exist = false)
    private Integer roomid;

    @TableField(exist = false)
    private Integer direction;

    //暗牌在手上
//    @TableField(exist = false)
//    private List<Integer> paiIn;

    @TableField(exist = false)
    private String paiIn;
    //出的牌
    @TableField(exist = false)
    private String paiOut;
    //明牌 (发财、碰了、扛、暗水)
    @TableField(exist = false)
    private  String paiShow;

    @TableField(exist = false)
    private Integer laizi;

    @TableField(exist = false)
    private Integer newpai;

    @TableField(exist = false)
    private Integer operation;

    @TableField(exist = false)
    private boolean show;

//    @TableField(exist = false)
//    private WebSocketServer webSocketServer;
//    public String getPai(){
//        List<Integer> paiIn = this.getPaiIn();
//        String result="";
//        for (Integer integer:paiIn ) {
//            result+=integer;
//        }
//        return result;
//    }

}
