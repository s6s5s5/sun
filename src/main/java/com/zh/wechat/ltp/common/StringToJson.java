package com.zh.wechat.ltp.common;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

/**
 * @author LiJun
 * @version 1.0
 * @date 2019/9/25 11:36
 */
@Service
@Log4j2
public class StringToJson {

    public JSONObject StringToJsonobject(HttpServletRequest request){
        log.info("URL请求地址："+request.getRequestURL());
        BufferedReader buff = null;
        StringBuffer bf = null;
        JSONObject jsonArray = null;
        try {
            buff = request.getReader();
            BufferedReader in = null;
            String result = "";
            bf = new StringBuffer();
            while ((result = buff.readLine()) != null) {
                bf.append(result);
            }
            jsonArray = JSONObject.parseObject(bf.toString());
            log.info("获取请求参数："+jsonArray.toJSONString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonArray;
    }
}
