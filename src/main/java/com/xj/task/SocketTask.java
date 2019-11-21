package com.xj.task;

import com.alibaba.fastjson.JSON;
import com.xj.controller.WebSocketServer;
import com.xj.vo.DensityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Copyright © 广州禾信仪器股份有限公司. All rights reserved.
 *
 * @Author hxsdd-20
 * @Date 2019/11/19 9:21
 * @Version 1.0
 */
@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SocketTask {

    @Autowired
    private List<DensityVo> list;

    public static int i=39;
    //3.添加定时任务
    //@Scheduled(cron = "0/5 * * * * ?")
    //或直接指定时间间隔，例如：20分钟
    @Scheduled(fixedRate = 500)
    private void pointTasks() {
//        当前连接数
        int onlineCount = WebSocketServer.getOnlineCount();
        if(onlineCount>0&&i<list.size()){
            DensityVo densityVo = list.get(i);
            String jsonstr= JSON.toJSONString(densityVo);
            try {
                WebSocketServer.sendInfo(jsonstr,"20");
                i++;
            } catch (IOException e) {
                System.out.println("发送消息失败");
                e.printStackTrace();
            }
        }
    }
}
