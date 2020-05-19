package com.xj.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xj.vo.DensityVo;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Copyright © 广州禾信仪器股份有限公司. All rights reserved.
 *
 * @Author hxsdd-20
 * @Date 2019/11/19 9:22
 * @Version 1.0
 */
//@Component
//public class DataInit {
//    @Bean
//    public List<DensityVo> getList(){
//        try {
//            // 根据resource文件路径，生成文件
//            File jsonFile = ResourceUtils.getFile("classpath:data/zh_real.json");
//            // 解析文件为指定编码的字符串
//            // 方法实现：先将文件转inPutStream，再调用下面的IOUtils.toString()方法；
//            String json = FileUtils.readFileToString(jsonFile,"UTF-8");
//            List<DensityVo> densityVos = JSON.parseArray(json, DensityVo.class);
//            return densityVos;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//}
