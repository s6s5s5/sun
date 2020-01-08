package com.zh.wechat.ltp.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;


/**
 *
 */
@Service
public class SimKeyCompare {
    /**
     * 将根据余弦值计算相似度 改为 根据两个值在文档中英文对应是否一致判断
     * 一致：0.93 否：0.3
     * @param
     * @return
     */
    public float CosineCompare(String keyword,String simword,HashMap<String,String> resultMap){
        /**
         * 根据关键词查找Map对应的value值，判断value值是否一致 一致视为相似返回0.93 否：0.3
         */
        String keyValue="";
        String SimValue="";
        try {
            if(keyword.equals(simword)){
                return 1f;
            }else {
                //HashMap<String, String> resultMap = ReadAndWriteFile();
                keyValue = resultMap.get(keyword) == null ? null : resultMap.get(keyword);
                SimValue = resultMap.get(simword) == null ? null : resultMap.get(simword);
                if (keyValue == null || SimValue == null) {
                    return 0.3f;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return keyValue.equals(SimValue)?0.93f:0.3f;
    }

}
