package com.xj.controller;

/**
 * Copyright © 广州禾信仪器股份有限公司. All rights reserved.
 *
 * @Author hxsdd-20
 * @Date 2019/11/19 9:35
 * @Version 1.0
 */

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.annotation.PostConstruct;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xj.common.MaJiangData;
import com.xj.entity.Room;
import com.xj.entity.User;
import com.xj.service.RoomService;
import com.xj.service.UserService;
import com.xj.task.SocketTask;
import com.xj.util.MaJiangUtil;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;


@ServerEndpoint("/websocket/{sid}")
@Component
public class WebSocketServer {

    private static WebSocketServer webSocketServer;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @PostConstruct
    public void init() {
        webSocketServer = this;
        webSocketServer.userService = this.userService;
        webSocketServer.roomService = this.roomService;
    }


    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

    private static Map<String, WebSocketServer> webSocketServerMap = new HashMap<>();
    //房间
//    public static Map<Integer, Room> roomMap=new HashMap<>();

//    public static Map<String,WebSocketServer> wsMap=new HashMap<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //接收sid
    private String sid = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
//        info="{"+info+"}";
//        JSONObject jsonObject=JSONObject.parseObject(info);
//        String sid=jsonObject.getString("sid");
//        Integer roomid=jsonObject.getInteger("roomid");
//        User user=webSocketServer.userService.selectById(sid);
//        user.setRoomid(roomid);
        webSocketServerMap.put(sid, this);
//        if(MaJiangData.roomMap.containsKey(roomid)){
//            Room room = MaJiangData.roomMap.get(roomid);
//            user.setRoomid(roomid);
//            boolean full1 = MaJiangData.isFull(roomid);
//            if(full1){
//                //当前房间已满 提示
//                onClose();
//                return;
//            }
//            MaJiangData.userMap.put(sid,user);
//            boolean full2 = MaJiangData.isFull(roomid);
//            if(full2){
//                //人员凑齐,客户端开始，并且发牌、产生癞子
//                MaJiangData.startMessage(roomid);
//            }
//        }else{
//            Room room = webSocketServer.roomService.selectById(roomid);
//            MaJiangData.userMap.put(sid,user);
//            MaJiangData.roomMap.put(roomid,room);
//        }
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新窗口开始监听:" + sid + ",当前在线人数为" + getOnlineCount());
        this.sid = sid;
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
//        SocketTask.i = 40;
//        MaJiangData.userMap.remove(sid); //重连
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("收到来自窗口" + sid + "的信息:" + message);
        JSONObject jsonObject = JSONObject.parseObject(message);
        int type = -1;
        if (jsonObject.containsKey("type")) {
            type = jsonObject.getInteger("type");
        }
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        //当前用户id
        String sid = jsonObject1.getString("sid");
        //当前房间号
        Integer roomid = jsonObject1.getInteger("roomid");
        //连接成功
        if (type == 0) {
            User user = webSocketServer.userService.selectById(sid);
            user.setRoomid(roomid);
            if (MaJiangData.roomMap.containsKey(roomid)) {
                Room room = MaJiangData.roomMap.get(roomid);
                //是否已经开始
                if(room.isStart()){
                    //将之前的牌发给他，不再重新发牌
                    MaJiangData.repeatPais(roomid,sid);
                    return;
                }
                boolean full1 = MaJiangData.isFull(roomid);
                if (full1) {
                    //当前房间已满 提示
                    //发消息 type=3
                    onClose();
                    return;
                }
                MaJiangData.userMap.put(sid, user);
                boolean full2 = MaJiangData.isFull(roomid);
                if (full2) {
                    //人员凑齐,客户端开始，并且发牌、产生癞子
                    MaJiangData.startMessage(roomid);
                }
            } else {
                Room room = webSocketServer.roomService.selectById(roomid);
                MaJiangData.userMap.put(sid, user);
                MaJiangData.roomMap.put(roomid, room);
            }
            sendCurrentLoginInfo(roomid);
        }else if(type==3){//出牌后的更新
            Integer paiid = jsonObject1.getInteger("paiid");
            Room room = MaJiangData.roomMap.get(roomid);
            //播放声音
            room.setVoiceType(MaJiangUtil.getVoiceType(paiid));
            if(paiid!=29){
                room.setNewpai(paiid);
                //最新出牌人
                room.setSid(sid);
            }
            MaJiangData.roomMap.put(roomid,room);
            //当前出牌人
            User user = MaJiangData.userMap.get(sid);
            String paiIn = user.getPaiIn();
            //是发财
            if(paiid==29){
                MaJiangData.refreshFaCai(roomid,sid);
                return;
            }else{
                if(user.getPaiOut()==null||user.getPaiOut().isEmpty()){
                    user.setPaiOut(paiid+"");
                }else{
                    user.setPaiOut(user.getPaiOut()+","+paiid);
                }
            }

            List<String> strings = Arrays.asList(paiIn.split(","));
            String paiInNew="";
            boolean isFirst=true;
            //出的牌就是新抓的牌
            if(user.getNewpai()!=null&&user.getNewpai().equals(paiid)){
                user.setNewpai(null);
                isFirst=false;
            }

            for (int i = 0; i < strings.size(); i++) {
                if(strings.get(i).equals(paiid+"")&&isFirst){
                    isFirst=false;
                    continue;
                }
                paiInNew+=strings.get(i)+",";
            }
            //出完牌后，清除新抓的牌,放在手牌里
            String substring = paiInNew.substring(0, (paiInNew.length() - 1));
            if(user.getNewpai()!=null){
                substring+=","+user.getNewpai();
                user.setNewpai(null);
            }
            user.setPaiIn(substring);
            MaJiangData.userMap.put(sid,user);

            //判断有人是否可以操作
            boolean isOperation = MaJiangData.isOperation(roomid, sid, paiid);
            //如果有人操作可以操作则询问
            if(isOperation){
                MaJiangData.ask(roomid,sid,paiid);
                return;
            }
            //更新牌
            MaJiangData.refresh(roomid,sid,paiid);
        }else if(type==4){//碰牌
            User user=MaJiangData.userMap.get(sid);
            Room room = MaJiangData.roomMap.get(roomid);
            //播放声音
            room.setVoiceType("/qita/peng");
            MaJiangData.roomMap.put(room.getId(),room);
            //当前最新出的牌
            Integer newpai = room.getNewpai();
            //手牌
            String[] paiIns = user.getPaiIn().split(",");
            //已碰牌、杠牌、发财、暗水
            String paishow=user.getPaiShow()==null?(newpai+""):(user.getPaiShow()+","+newpai);
            String paiin="";
            int count=0;
            for (String pai:paiIns ) {
                if(pai.equals(newpai+"")&&count<2){
                    if(paishow.equals("")){
                        paishow+=pai;
                    }else{
                        paishow+=","+pai;
                    }
                    count++;
                }else{
                    if(paiin.equals("")){
                        paiin+=pai;
                    }else{
                        paiin+=","+pai;
                    }
                }
            }
            user.setPaiShow(paishow);
            user.setPaiIn(paiin);
            user.setCanChuPai(true);
            MaJiangData.userMap.put(sid,user);
            MaJiangData.refreshPeng(roomid,sid);
        }else if(type==5){//杠牌
            User user=MaJiangData.userMap.get(sid);
            Room room = MaJiangData.roomMap.get(roomid);

            //播放声音
            room.setVoiceType("/qita/gang");
            MaJiangData.roomMap.put(room.getId(),room);

            //最新出的牌
            Integer newpai = room.getNewpai();

            boolean canAfterGang = MaJiangUtil.afterGang(user.getPaiIn(), user.getNewpai(), user.getPaiShow());
            if(user.getOperation()==4&&canAfterGang){
                String afterGang = MaJiangUtil.getAfterGang(user.getPaiIn(), user.getNewpai(), user.getPaiShow());
                user.setPaiShow(user.getPaiShow()+","+afterGang);
                String paiInNew=user.getPaiIn()+(user.getNewpai()==null?"":","+user.getNewpai());
                user.setPaiIn(MaJiangUtil.removeStr(paiInNew,afterGang));
            }else{
                String[] paiIns = user.getPaiIn().split(",");
                String paishow=user.getPaiShow()==null?(newpai+""):(user.getPaiShow()+","+newpai);
                String paiin="";
                int count=0;
                for (String pai:paiIns ) {
                    if(pai.equals(newpai+"")&&count<3){
                        if(paishow.equals("")){
                            paishow+=pai;
                        }else{
                            paishow+=","+pai;
                        }
                        count++;
                    }else{
                        if(paiin.equals("")){
                            paiin+=pai;
                        }else{
                            paiin+=","+pai;
                        }
                    }
                }
                user.setPaiShow(paishow);
                user.setPaiIn(paiin);
            }
            MaJiangData.userMap.put(sid,user);
            MaJiangData.refreshGang(roomid,sid);
        } else if(type==8){
            //过
            Room room = MaJiangData.roomMap.get(roomid);
            room.setVoiceType(null);
            MaJiangData.roomMap.put(roomid,room);

            User user = MaJiangData.userMap.get(sid);
            user.setOperation(null);
            MaJiangData.userMap.put(user.getId()+"",user);

            Integer newpai = room.getNewpai();
            String sidcurrent=room.getSid();

            MaJiangData.refresh(roomid,sidcurrent,null);
        }else if(type==9){
            //下一局
            boolean full2 = MaJiangData.isFull(roomid);
            if (full2) {
                //人员凑齐,客户端开始，并且发牌、产生癞子
                MaJiangData.startMessage(roomid);
            }
        }else if(type==10){//暗杆
            MaJiangData.refreshAnGang(roomid,sid);
        }else if(type==11){
            MaJiangData.refreshMingPai(roomid,sid);
        }

        //群发消息
//        for (WebSocketServer item : webSocketSet) {
//            try {
//                item.sendMessage(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message, @PathParam("sid") String sid) throws IOException {
        System.out.println("推送消息到窗口" + sid + "，推送内容:" + message);
        for (WebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if (sid == null) {
                    item.sendMessage(message);
                } else if (item.sid.equals(sid)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }
    //当前在线人数
    public static synchronized void sendCurrentLoginInfo(Integer roomid) {
        String msg = MaJiangData.currentInfo(roomid);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "0");
            jsonObject.put("data", "连接成功,当前人数" + msg);
            sendInfo(jsonObject.toJSONString(),null);
        } catch (IOException e) {
            System.out.println("websocket IO异常");
        }
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    //发牌
    public static void faPai(User user,Integer type) throws IOException {
        WebSocketServer webSocketServer = webSocketServerMap.get(user.getId() + "");
        Room room = MaJiangData.roomMap.get(user.getRoomid());
        Map<String, User> userMap = MaJiangData.userMap;
        List<User> userList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray=new JSONArray();
        for (String key : userMap.keySet()) {
            User user1 = userMap.get(key);
            if (user1.getRoomid().equals(room.getId())) {
                userList.add(user1);
            }
            room.setUsers(userList);
        }
//        String message=user.getPaiIn();
        String message = JSONObject.toJSONString(user);

        jsonObject.put("type", type);//刷新牌
        jsonObject.put("data", user);
        jsonObject.put("room", JSONObject.toJSONString(room, SerializerFeature.DisableCircularReferenceDetect));
        String msg = jsonObject.toJSONString();
        System.out.println(msg);
        webSocketServer.sendMessage(msg);
    }
//    public static synchronized void startMessage(Integer roomid){
//        Room room = MaJiangData.roomMap.get(roomid);
//        List<User> users = room.getUsers();
//        List<Integer> allPai = MaJiangData.getAllPai();
//        int random = (int)(0+Math.random()*(3+1));
//        int temp=13;
//        //癞子
//        Integer laiZi = MaJiangData.getLaiZi(allPai.get(13 * 3 + 14));
//        for (int i = 0; i < users.size(); i++) {
//            int paiSize=13;
//            User user=users.get(i);
////            WebSocketServer webSocketServer = wsMap.get(""+user.getId());
//            WebSocketServer webSocketServer = user.getWebSocketServer();
//            if(random==i){
//                paiSize=14;
//                user.setMaster(true);
//            }else {
//                user.setMaster(false);
//            }
//            user.setLaizi(laiZi);
//            temp=paiSize;
//            List<Integer> integers = allPai.subList(0+i*paiSize, temp+i*paiSize);
//            user.setPaiIn(integers);
//            //移除牌
//            MaJiangData.removeList(temp,allPai);
//
////            String msg="";
////            msg="12311111111";
////            try {
////                webSocketServer.sendMessage(msg);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//        }
//        room.setPais(allPai);
//    }


}


