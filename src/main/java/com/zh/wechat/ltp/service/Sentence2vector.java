package com.zh.wechat.ltp.service;


import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class Sentence2vector {

    @Resource
    SimKeyCompare simKeyCompare = new SimKeyCompare();

    public float calSimilarity(String sentence1, String sentence2, HashMap<String,String> resultMap) {
        String[] termList1 = sentence1.replace("[","").replace("]","").replaceAll("\\s*", "").split(",");
        String[] termList2 = sentence2.replace("[","").replace("]","").replaceAll("\\s*", "").split(",");
        float temp = 0;
        float sum_sim = 0;
        int i = 0;
        for (String t1 : termList1) {
            float max_sim = 0;
            Map<String,String> tMap = new HashMap<String,String>();
            for (String t2 : termList2) {
                float sim = simKeyCompare.CosineCompare(t1.trim(),t2.trim(),resultMap);
                if (sim == 1 || sim == 0.93) {
                    if(tMap != null){

                    }else{
                        tMap.put("key",t1);
                        i++;
                    }
                }
                if (sim > max_sim) {
                    max_sim = sim;
                }
            }
            sum_sim += max_sim;
        }

        if(i==termList1.length || i==termList2.length ){
            return temp = 0.93f;
        }else{
            return temp = sum_sim/termList1.length;
        }
    }

}
