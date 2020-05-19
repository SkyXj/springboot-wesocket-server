package com.xj.common;

import com.alibaba.fastjson.JSONObject;
import com.xj.controller.WebSocketServer;
import com.xj.entity.LinkRoomUser;
import com.xj.entity.Room;
import com.xj.entity.User;
import com.xj.util.MaJiangUtil;
import io.swagger.models.auth.In;

import java.io.IOException;
import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

public class MaJiangData {

    //几个人就满房了
    private static int total=4;
    //房间
    public static Map<Integer, Room> roomMap = new HashMap<>();
    //人
    public static Map<String, User> userMap = new HashMap<>();
    //房间
    public static Map<Integer, List<Integer>> roomDirection = new HashMap<>();

    public static List<Integer> getAllPai() {
        //湖北麻将只有万(1-9)、条(10-18)、筒(19-27)、中(28)、发(29)、白(30)
        List<Integer> pais = new ArrayList<Integer>();
        for (int i = 1; i <= 30; i++) {
            for (int j = 0; j < 4; j++) {
                pais.add(i);
            }
        }
//        Collections.shuffle(pais);
        //测试
//        pais.add(1);
//        pais.add(1);
//        pais.add(1);
//        pais.add(1);
//
//        pais.add(2);
//        pais.add(2);
//        pais.add(2);
//        pais.add(3);
//
//        pais.add(4);
//        pais.add(4);
//        pais.add(4);
//        pais.add(5);
//
//        pais.add(5);
//
//
//        pais.add(7);
//        pais.add(7);
//        pais.add(7);
//        pais.add(7);
//
//        pais.add(8);
//        pais.add(8);
//        pais.add(8);
//        pais.add(9);
//
//        pais.add(10);
//        pais.add(10);
//        pais.add(10);
//        pais.add(16);
//
//        pais.add(16);
//
//
//        pais.add(11);
//        pais.add(11);
//        pais.add(11);
//        pais.add(11);
//
//        pais.add(12);
//        pais.add(12);
//        pais.add(12);
//        pais.add(13);
//
//        pais.add(14);
//        pais.add(14);
//        pais.add(14);
//        pais.add(15);
//
//        pais.add(15);
//
//        pais.add(17);
//        pais.add(17);
//        pais.add(17);
//        pais.add(17);
//
//        pais.add(18);
//        pais.add(18);
//        pais.add(18);
//        pais.add(19);
//
//        pais.add(20);
//        pais.add(20);
//        pais.add(20);
//        pais.add(21);
//
//        pais.add(22);
//        pais.add(22);
//        pais.add(22);
//        pais.add(23);
        return pais;
    }

    //所有牌(每个牌只有一个)
    public static List<Integer> getAllPaiSimple() {
        //湖北麻将只有万(1-9)、条(10-18)、筒(19-27)、中(28)、发(29)、白(30)
        List<Integer> pais = new ArrayList<Integer>();
        for (int i = 1; i <= 30; i++) {
            pais.add(i);
        }
        return pais;
    }

    //移除前n个元素
    public static void removeList(int n, List<Integer> list) {
        for (int i = 0; i < 3; i++) {
            list.remove(0);
        }
    }

    public static Integer getLaiZi(Integer integer) {
        if (integer >= 1 && integer < 29) {
            if (integer == 9) {
                return 1;
            }
            if (integer == 18) {
                return 10;
            }
            if (integer == 27) {
                return 19;
            }
            return integer + 1;
        }
        if (integer == 28 || integer ==29) {
            return 30;
        } else {
            return 28;
        }
    }

    //人数是否满了
    public static boolean isFull(Integer roomid) {
        int count = 0;
        for (String key : userMap.keySet()) {
            User user = userMap.get(key);
            if (user.getRoomid().equals(roomid)) {
                count++;
            }
        }
        if (count >= total) {
            return true;
        } else {
            return false;
        }
    }
    //开始游戏
    public static void startMessage(Integer roomid) {
        roomDirection=new HashMap<>();
        Room room = MaJiangData.roomMap.get(roomid);
        List<Integer> allPai = MaJiangData.getAllPai();
        int random = (int) (0 + Math.random() * (3 + 1));
        int temp = 13;
        //癞子
        Integer laiZi = MaJiangData.getLaiZi(allPai.get(13 * 3 + 14));
        allPai.remove(13 * 3 + 14);
        List<User> users = getList(roomid);
        for (int i = 0; i < users.size(); i++) {
            int paiSize = 13;
            User user = users.get(i);
            //重置
            user.setNewpai(null);
            user.setPaiShow(null);
            user.setCanChuPai(false);
            user.setPaiOut(null);
//            user.setDirection();
            user.setOperation(null);
            user.setMaster(false);
            user.setShow(false);

            if(room.getZhuangid()!=null){
                if (room.getZhuangid().equals(user.getId()+"")) {
                    paiSize = 14;
                    user.setMaster(true);
                    user.setCanChuPai(true);
                } else {
                    user.setMaster(false);
                }
            }else{
                if (random == i) {
                    paiSize = 14;
                    user.setMaster(true);
                    user.setCanChuPai(true);
                } else {
                    user.setMaster(false);
                }
            }
//            user.setLaizi(laiZi);
            temp = paiSize;
            List<Integer> integers = allPai.subList(0 + i * paiSize, temp + i * paiSize);
            allPai=getNewAllPai(0 + i * paiSize, temp + i * paiSize,allPai);
//            //替换发财
//            for (Integer integer:integers) {
//                if(integer==29){
//                    String[] pais = getPai(allPai, "@").split("@");
//                    integer=Integer.parseInt(pais[0]);
//                    if(pais.length>1){
//                        user.setPaiShow(user.getPaiShow()==null?""+pais[1]:user.getPaiShow()+","+pais[1]);
//                    }
//                }
//            }

//            String s = replaceFaCai(integers, allPai, "");
//            user.setPaiShow(s);
//            if(user.getPaiShow()!=null&&user.getPaiShow().length()>0){
//                user.setPaiShow(user.getPaiShow().substring(0,user.getPaiShow().length()-2));
//            }
            //获取方位
            if(user.getDirection()==null){
                Integer diretion = getDiretion(roomid);
                if(roomDirection.containsKey(roomid)){
                    roomDirection.get(roomid).add(diretion);
                }else{
                    List<Integer> list=new ArrayList<>();
                    list.add(diretion);
                    roomDirection.put(roomid,list);
                }
                //设置方位
                user.setDirection(diretion);
            }
            //将牌转换为str
            String str = getStr(integers);
//            //替换发财
//            String paiin="";
//            String[] split = str.split(",");
//            for (int j = 0; j < split.length; j++) {
//                String s = split[j];
//                if(s.equals("29")){
//                    String[] paitemp = getPai(allPai, "@").split("@");
//                    s=paitemp[0];
//                    if(paitemp.length>1){
//                        user.setPaiShow(user.getPaiShow()==null?""+paitemp[1]:user.getPaiShow()+","+paitemp[1]);
//                    }
//                }
//                split[j]=s;
//                if(paiin.isEmpty()){
//                    paiin+=s;
//                }else{
//                    paiin+=","+s;
//                }
//            }

//            if(user.isMaster()){
//                user.setNewpai(Integer.parseInt(split[split.length-1]));
//                paiin=paiin.substring(0,(paiin.lastIndexOf(",")));
//            }

           if(user.isMaster()){
               boolean canAnShui = MaJiangUtil.isCanAnShui(str);
               if(canAnShui){
                   user.setOperation(2);
                   user.setCanChuPai(false);
               }
           }
            user.setPaiIn(str);
            userMap.put(user.getId()+"",user);
        }
        room.setPais(allPai);
        room.setStart(true);
        room.setQiaoBao(laiZi);
        room.setVoiceType(null);

        roomMap.put(roomid,room);
        for (User user:users ) {
            try {
                WebSocketServer.faPai(user,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        room.setUsers(users);
    }

    public static List<Integer> getNewAllPai(int start,int end,List<Integer> allpai){
        List<Integer> result=new ArrayList<>();
        for (int i = 0; i < allpai.size(); i++) {
            if(i<start||i>=end){
                result.add(allpai.get(i));
            }
        }
        return result;
    }

    //当前用户刷新牌
    public static void repeatPais(Integer roomid,String sid){
        List<User> users = getList(roomid);
        for (User user:users ) {
            try {
                if(sid.equals(user.getId()+"")){
                    WebSocketServer.faPai(user,1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //询问是否可以操作
    public static boolean isOperation(Integer roomid,String sid,Integer paiid){
        boolean isOperation=false;
        List<User> users = getList(roomid);
        for (User user : users) {
            //自己的出的牌，不看他是否可以碰和杠
            if(sid.equals(user.getId()+"")){
                user.setOperation(-1);
                userMap.put(user.getId()+"",user);
                continue;
            }
            int size= MaJiangUtil.repeatCount(user.getPaiIn(),paiid);
            if(size==3){
                //操作(-1,无法操作,0,碰,1 杠,2 暗水,3 是胡牌)
                user.setOperation(1);
                isOperation=true;
            }else if(size==2){
                //碰
                user.setOperation(0);
                isOperation=true;
            }else {
                user.setOperation(-1);
            }
            userMap.put(user.getId()+"",user);
            //是否可以胡
            //该算法过于复杂稍后实现
        }
        return isOperation;
    }

    public static void askGang(Integer roomid,String sid){
        List<User> users = getList(roomid);
        for (User user : users) {
            if(sid.equals(user.getId()+"")){
                user.setOperation(2);
//                String paiIn = user.getPaiIn();
//                if(user.getNewpai()!=null){
//                    paiIn+=paiIn+","+user.getNewpai();
//                }
//                String anShui = MaJiangUtil.getAnShui(paiIn);

            }
            user.setCanChuPai(false);
            userMap.put(user.getId()+"",user);
            //询问
            try {
                WebSocketServer.faPai(user,4);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void ask(Integer roomid,String sid,Integer paiid){
        List<User> users = getList(roomid);
        for (User user : users) {
            //自己的出的牌，不看他是否可以碰、和扛
            if(sid.equals(user.getId()+"")){
                user.setOperation(null);
                user.setCanChuPai(false);
                continue;
            }
            int size= MaJiangUtil.repeatCount(user.getPaiIn(),paiid);
            if(size==3){
                //可以杠(0,碰,1 杠,2 暗水,3 是胡牌)
                user.setOperation(1);
            }else if(size==2){
                //碰
                user.setOperation(0);
            }else{
                user.setOperation(-1);
            }
            userMap.put(user.getId()+"",user);
        }
        refreshAllPai(roomid);
    }

    public static void refreshPeng(Integer roomid,String sid){
        List<User> users = getList(roomid);
        for (User user:users ) {
            if(sid.equals(user.getId()+"")){
                user.setCanChuPai(true);
                user.setOperation(null);
            }else{
                user.setCanChuPai(false);
            }
            userMap.put(user.getId()+"",user);
            try {
                WebSocketServer.faPai(user,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void refreshGang(Integer roomid,String sid){
        List<User> users = getList(roomid);
        Room room = roomMap.get(roomid);
        List<Integer> pais = room.getPais();
        for (User user:users ) {
            if(sid.equals(user.getId()+"")){
                user.setCanChuPai(true);
                user.setOperation(null);
                if(pais.size()>0){
                    Integer integer = pais.get(0);
                    user.setNewpai(integer);
                    pais.remove(0);
                    room.setPais(pais);
                    roomMap.put(roomid,room);

                    //判断是否可以暗杆
                    boolean canAnShui = MaJiangUtil.isCanAnShui(user.getPaiIn() + "," + integer);

                    //判断是否可以杠已碰的牌
                    boolean canAfterGang = MaJiangUtil.afterGang(user.getPaiIn(), user.getNewpai(), user.getPaiShow());
                    if(canAnShui){
                        user.setCanChuPai(false);
                        user.setOperation(2);
                    }
                    if(canAfterGang){
                        user.setCanChuPai(false);
                        user.setOperation(4);
                    }
                    if(canAnShui&&canAfterGang){
                        user.setCanChuPai(false);
                        user.setOperation(5);
                    }
                }else{
                    user.setNewpai(-1);
                }
            }else{
                user.setCanChuPai(false);
            }
            userMap.put(user.getId()+"",user);
            try {
                WebSocketServer.faPai(user,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void refreshAnGang(Integer roomid,String sid){
        List<User> users=getList(roomid);
        Room room=roomMap.get(roomid);
        List<Integer> pais=room.getPais();
        for (User user:users) {
            if(sid.equals(user.getId()+"")){
                String paiIn = user.getPaiIn();
                String anShui = MaJiangUtil.getAnShui(paiIn);
                String[] split = paiIn.split(",");
                String newPaiIn=user.getNewpai()==null?"":user.getNewpai()+"";
                String paiShow=user.getPaiShow();
                for (String pai:split ) {
                    if(!pai.equals(anShui)){
                        if(newPaiIn==null||newPaiIn.isEmpty()){
                            newPaiIn+=pai;
                        }else{
                            newPaiIn+=","+pai;
                        }
                    }else{
                        if(paiShow==null||paiShow.isEmpty()){
                            paiShow=pai;
                        }else{
                            paiShow+=","+pai;
                        }
                    }
                }

                if(pais.size()>0){
                    Integer integer = pais.get(0);
                    pais.remove(0);
                    user.setNewpai(integer);
                }else{
                    user.setNewpai(-1);
                }
                user.setCanChuPai(true);
                user.setPaiIn(newPaiIn);
                user.setPaiShow(paiShow);
                user.setOperation(null);

                //判断是否可以暗杆
                boolean canAnShui = MaJiangUtil.isCanAnShui(user.getPaiIn() + "," + user.getNewpai());

                //判断是否可以杠已碰的牌
                boolean canAfterGang = MaJiangUtil.afterGang(user.getPaiIn(), user.getNewpai(), user.getPaiShow());
                if(canAnShui){
                    user.setCanChuPai(false);
                    user.setOperation(2);
                }
                if(canAfterGang){
                    user.setCanChuPai(false);
                    user.setOperation(4);
                }
                if(canAnShui&&canAfterGang){
                    user.setCanChuPai(false);
                    user.setOperation(5);
                }

//                //依然能暗水
//                boolean canAnShui = MaJiangUtil.isCanAnShui(user.getPaiIn() + "," + user.getNewpai());
//
//                if(canAnShui){
//                    user.setOperation(2);
//                    user.setCanChuPai(false);
//                }
            }else{
                user.setCanChuPai(false);
            }

            userMap.put(user.getId()+"",user);
        }
        room.setPais(pais);
        //播放声音
        room.setVoiceType("/qita/gang");
        roomMap.put(roomid,room);
        refreshAllPai(roomid);
    }

    public static void refreshMingPai(Integer roomid,String sid){
        List<User> users=getList(roomid);
        Room room=roomMap.get(roomid);
        room.setVoiceType("/qita/hu");
        MaJiangData.roomMap.put(room.getId(),room);
        room.setZhuangid(sid);
        for (User user:users ) {
            if(sid.equals(user.getId()+"")){
                user.setShow(true);
                userMap.put(user.getId()+"",user);
            }
        }
        refreshAllPai(roomid);
    }

    public static void refreshAllPai(Integer roomid){
        List<User> list = getList(roomid);
        for (User user:list) {
            try {
                WebSocketServer.faPai(user,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void refreshFaCai(Integer roomid,String sid){
        List<User> users = getList(roomid);
        Room room = roomMap.get(roomid);
        for (User user:users) {
            if(sid.equals(user.getId()+"")){
                if(user.getPaiShow()==null||user.getPaiShow().isEmpty()){
                    user.setPaiShow("29");
                }else{
                    user.setPaiShow(user.getPaiShow()+",29");
                }

                String paiIn = user.getPaiIn();
                List<String> strings = Arrays.asList(paiIn.split(","));
                String paiInNew="";
                boolean isFirst=true;
                //出的牌就是新抓的牌
                if(user.getNewpai()!=null&&"29".equals(user.getNewpai()+"")){
                    user.setNewpai(null);
                    isFirst=false;
                }

                for (int i = 0; i < strings.size(); i++) {
                    if(strings.get(i).equals("29")&&isFirst){
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


                List<Integer> pais = room.getPais();
                if(pais.size()>0){
                    Integer integer = pais.get(0);
                    pais.remove(0);
                    user.setNewpai(integer);
                    user.setCanChuPai(true);
                    room.setPais(pais);
                    room.setVoiceType(null);
                    MaJiangData.roomMap.put(roomid,room);
                }else{
                    user.setNewpai(-1);
                }

                //判断是否可以暗杆
                boolean canAnShui = MaJiangUtil.isCanAnShui(user.getPaiIn() + "," + user.getNewpai());

                //判断是否可以杠已碰的牌
                boolean canAfterGang = MaJiangUtil.afterGang(user.getPaiIn(), user.getNewpai(), user.getPaiShow());
                if(canAnShui){
                    user.setCanChuPai(false);
                    user.setOperation(2);
                }
                if(canAfterGang){
                    user.setCanChuPai(false);
                    user.setOperation(4);
                }
                if(canAnShui&&canAfterGang){
                    user.setCanChuPai(false);
                    user.setOperation(5);
                }

            }else{
                user.setCanChuPai(false);
            }
            userMap.put(user.getId()+"",user);
        }
        refreshPai(roomid);
    }

    public static void refreshPai(Integer roomid){
        List<User> users = getList(roomid);
        for (User user:users ) {
            try {
                WebSocketServer.faPai(user,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //刷新牌
    public static void refresh(Integer roomid,String sid,Integer paiid){
        List<User> users = getList(roomid);
        User userMain=MaJiangData.userMap.get(sid);
        Integer direction = userMain.getDirection();
        //下家
        Integer directionNext;
        if(direction==3){
            directionNext=0;
        }else{
            directionNext=direction+1;
        }
        for (User user:users ) {
//            try {
                //是下一家
                if(user.getDirection().equals(directionNext)){
                    //抓牌
                    Room room = roomMap.get(roomid);
                    List<Integer> pais = room.getPais();
                    if(pais.size()>0){
                        Integer integer = pais.get(0);
                        pais.remove(0);
                        user.setNewpai(integer);
                        room.setPais(pais);
                        roomMap.put(roomid,room);
                        user.setCanChuPai(true);
                        user.setOperation(null);
                        //判断是否可以暗杆
                        boolean canAnShui = MaJiangUtil.isCanAnShui(user.getPaiIn() + "," + integer);

                        //判断是否可以杠已碰的牌
                        boolean canAfterGang = MaJiangUtil.afterGang(user.getPaiIn(), user.getNewpai(), user.getPaiShow());

                        if(canAnShui){
                            user.setCanChuPai(false);
                            user.setOperation(2);
                        }

                        if(canAfterGang){
                            user.setCanChuPai(false);
                            user.setOperation(4);
                        }

                        if(canAnShui&&canAfterGang){
                            user.setCanChuPai(false);
                            user.setOperation(5);
                        }

                    }else{
                        user.setNewpai(-1);
                    }
                }else{
                    user.setCanChuPai(false);

                }
//                WebSocketServer.faPai(user,1);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            MaJiangData.userMap.put(user.getId()+"",user);
        }
        refreshAllPai(roomid);
    }

    public static List<User> getList(Integer roomid) {
        List<User> list = new ArrayList<>();
        for (String key : userMap.keySet()) {
            User user = userMap.get(key);
            if (user.getRoomid().equals(roomid)) {
                list.add(user);
            }
        }
        return list;
    }

    public static String getStr(List<Integer> integers){
        String str="";
        for (Integer integer : integers) {
            str+=integer+",";
        }
        return str.substring(0,str.length()-1);
    }

    //前面是最后的牌后面是最后发财的个数
    public static String getPai(List<Integer> pais,String result){
//        String result="|";
        if(pais.size()>0){
            Integer integer = pais.get(0);
            pais.remove(0);
            if(integer==29){
                if(result.length()-1>result.indexOf("@")){
                    result=result+",29";
                }else{
                    result=result+"29";
                }
                result=getPai(pais,result);
            }else{
                result=integer+result;
            }
            return result;
        }else {
            result="-1"+result;
            return result;
        }
    }


    public static void main(String[] args) {
        List<Integer> pais=new ArrayList<>();
        pais.add(5);
        pais.add(2);
        pais.add(3);
        pais.add(4);
        pais.add(5);
        System.out.println(pais.size());
        String pai = getPai(pais, "@");
        System.out.println(pais.size());
        System.out.println(pai);

//        List<Integer> integers = pais.subList(1, 3);
//        List<Integer> newPai=getNewAllPai(1,3,pais);
//
//        System.out.println(pais.size());
//        System.out.println(getPai(pais,"|"));
//        System.out.println(pais.size());
    }
    //替换发财
    public static String replaceFaCai(List<Integer> integers,List<Integer> allPai,String facai){
        //如果没有发财
        if(integers.indexOf(29)<0){
            if(facai.length()>0){
                facai=facai.substring(0,facai.length()-2);
            }
            return facai;
        }
        for (int j = 0; j < integers.size(); j++) {
            Integer ss=integers.get(j);
            if(ss.equals(29)){
                Integer integer = allPai.get(0);
                integers.set(j,integer);
                MaJiangData.removeList(1, allPai);
                facai+=29+",";
            }
        }
        return replaceFaCai(integers,allPai,facai);
    }

//    public static String getReplace(List<Integer> allPai){
//        String facai="";
//        Integer integer = allPai.get(0);
//        MaJiangData.removeList(1,allPai);
//        if(integer.equals("29")){
//            facai+=getReplace(allPai);
//        }else{
//            facai+=29+",";
//        }
//        return facai;
//    }

    public static Integer getDiretion(Integer roomid){
        int random = (int) (0 + Math.random() * (3 + 1));
        if(roomDirection!=null&&roomDirection.containsKey(roomid)){
            boolean contains = roomDirection.get(roomid).contains(random);
            if(contains){
                return getDiretion(roomid);
            }
        }
        return random;
    }

    private boolean isCanHu(){
        return false;
    }


    public static String currentInfo(Integer roomid){
        List<User> list = getList(roomid);
        String result=list.size()+"";
        if(list.size()>0){
            result+=",分别是：";
            for (User user:list) {
                result+=user.getNickname()+"、";
            }
            result=result.substring(0,(result.length()-1));
        }
        return result;
    }
}
