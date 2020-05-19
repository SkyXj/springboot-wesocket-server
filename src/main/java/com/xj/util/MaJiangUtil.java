package com.xj.util;

import com.xj.common.MaJiangData;
import io.swagger.models.auth.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
public class MaJiangUtil {

    public static List<String> getTingPais(String pais,Integer laizi){
        return null;
    }

    //是否可以胡牌
    public static boolean isHub(String pais,String newPai,Integer laizi,boolean isZimo){
        String[] strings=pais.split(",");
        List<String> tempList= Arrays.asList(strings);
        List<String> paiList = new ArrayList(tempList);

        if(newPai!=null&&!newPai.isEmpty()){
            paiList.add(newPai);
        }
        //是否是七对
        boolean qiDui = isQiDui(paiList,laizi);
        if(qiDui){
            return true;
        }
        //是否是碰碰胡
        boolean pengPengHu=isPengPengHu();
        //是否是清一色
        boolean qingYiSe=isQingYiSe();
        //普通胡
        boolean puTongHu=isPuTongHu(paiList,newPai,laizi,isZimo);
        return false;
    }

    private static boolean isPuTongHu(List<String> pais,String newPai,Integer laizi,boolean isZimo) {
        //不包含癞子的牌
        List<Integer> paiList=pais.stream().filter(t->!t.equals(laizi+"")).map(x->Integer.parseInt(x)).collect(Collectors.toList());
        //癞子的个数
        Integer laiZiCount=paiList.stream().filter(t->t.equals(newPai)).collect(Collectors.toList()).size();
        //万的个数
        List<Integer> wans=paiList.stream().filter(t->t>=1&&t<=9).collect(Collectors.toList());
        Integer wanNeedLai=getNeedLaiZiNum(wans);
        //条的个数
        List<Integer> tiaos=paiList.stream().filter(t->t>=10&&t<=18).collect(Collectors.toList());
        Integer tiaoNeedLai=getNeedLaiZiNum(tiaos);
        //筒的个数
        List<Integer> tongs=paiList.stream().filter(t->t>=19&&t<=27).collect(Collectors.toList());
        Integer tongNeedLai=getNeedLaiZiNum(tongs);

        return false;
    }

    //得到某个花色成为整扑需要的癞子个数
    private static Integer getNeedLaiZiNum(List<Integer> pais){
        return 0;
    }

    //是否七对
    public static boolean isQiDui(List<String> paiList,Integer laizi){
        int count=(int)paiList.stream().filter(t->t.equals(laizi+"")).count();
        //需要几个对
        int duis=7-count;
        int index=0;
        Integer[] integers=new Integer[30];
        List<Integer> allPai = MaJiangData.getAllPaiSimple();
        for (int i = 0; i < allPai.size(); i++) {
            for (int j = 0; j < paiList.size(); j++) {
                if(paiList.get(j).equals(allPai.get(i)+"")&&!paiList.get(j).equals(laizi+"")){
                    integers[i]=integers[i]==null?1:integers[i]+1;
                }
            }
        }
        List<Integer> temp=Arrays.asList(integers);

        List<Integer> result=new ArrayList<>(temp);

        int total=0;
        for (Integer integer:result ) {
            if(integer!=null){
                total+=integer/2;
            }
        }

        if(total>=duis){
            return true;
        }
        return false;
    }
    //是否碰碰胡
    public static boolean isPengPengHu(){
        return true;
    }
    //是否清一色
    public static boolean isQingYiSe(){
        return true;
    }

    public static void main(String[] args) {
//        String pais="1,1,1,2,2,2,3,3,3,5,5,5,5";
//        Integer laizi=5;
//        String newPai="7";
//        System.out.println(afterGang("1,1,2,4,5,13,14,14,20,21",27,"18,18,18"));
        System.out.println(afterGang("1,1,2,4,5,13,14,14,20,21",30,"30,30,30"));
    }

//    public static void isCanPeng(String strs,Integer paiid){
//
//    }

    public static boolean isCanAnShui(String str){
        String[] strings=str.split(",");
        List<String> list=new ArrayList(Arrays.asList(strings));
        for (String pai:strings) {
            int count=list.stream().filter(t->t.equals(pai)).collect(Collectors.toList()).size();
            if(count==4){
                return true;
            }
        }
        return false;
    }

    public static Integer countRepeat(String str,String charstr){
        if(str==null||str.length()<=0){
            return 0;
        }
        if(charstr==null||charstr.length()<=0){
            return 0;
        }
        int count=0;
        String[] split = str.split(",");
        for (String temp: split) {
            if(temp.equals(charstr)){
                count++;
            }
        }
        return count;
    }

    public static boolean afterGang(String paiIn,Integer paiNew,String paiShow){
        if(paiNew!=null){
            paiIn+=","+paiNew;
        }
        String[] pais=paiIn.split(",");
        for (String pai:pais) {
            Integer integer = countRepeat(paiShow, pai);
            //如果已经碰了，并且碰了的三个不是发财
            if(integer==3&&!pai.equals("29")){
                return true;
            }
        }
        return false;
    }


    public static String removeStr(String oldstr,String charstr){
        String[] pais=oldstr.split(",");
        String result="";
        Integer count=0;
        for (String pai:pais) {
            if(pai.equals(charstr)&&count<1){
                count++;
                continue;
            }else{
                if(result.isEmpty()){
                    result +=pai;
                }else{
                    result+=","+pai;
                }
            }
        }
        return result;
    }

    public static String getAfterGang(String paiIn,Integer paiNew,String paiShow){
        if(paiNew!=null){
            paiIn+=","+paiNew;
        }
        String[] pais=paiIn.split(",");
        for (String pai:pais) {
            Integer integer = countRepeat(paiShow, pai);
            //如果已经碰了，并且碰了的三个不是发财
            if(integer==3&&pai!="29"){
                return pai;
            }
        }
        return "";
    }

    public static String getAnShui(String str){
        String[] strings=str.split(",");
        List<String> list=new ArrayList(Arrays.asList(strings));
        for (String pai:strings) {
            int count=list.stream().filter(t->t.equals(pai)).collect(Collectors.toList()).size();
            if(count==4){
                return pai;
            }
        }
        return "-1";
    }

    public static int repeatCount(String strs,Integer paiid){
        int count=0;
        String[] paiIn = strs.split(",");
        for (String pai:paiIn ) {
            if(pai.equals(paiid+"")){
                count++;
            }
        }
        return count;
    }

    //得到播放声音的类型
    public static String getVoiceType(Integer paiId){
        if(paiId>=1&&paiId<=9){
            return "/wan/"+paiId+"wan";
        }else if(paiId>=10&&paiId<=18){
            return "/tiao/"+(paiId-9)+"tiao";
        }else if(paiId>=19&&paiId<=27){
            return "/tong/"+(paiId-18)+"tong";
        }else if(paiId==28){
            return "/qita/hongzhong";
        }else if(paiId==30){
            return "/qita/baiban";
        }
        return null;
    }
}
