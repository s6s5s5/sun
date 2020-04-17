package com.zh.wechat.ltp.service;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class Sentence2vector {

    @Resource
    public SimKeyCompare simKeyCompare;

    public float calSimilarity(String sentence1, String sentence2) {
        String[] termList1 = sentence1.replace("[","").replace("]","").replaceAll("\\s*", "").split(",");
        String[] termList2 = sentence2.replace("[","").replace("]","").replaceAll("\\s*", "").split(",");
        float temp = 0;
        float sum_sim = 0;
        int i = 0;
        for (String t1 : termList1) {
            float max_sim = 0;
            Map<String,String> tMap = new HashMap<String,String>();
            for (String t2 : termList2) {
                float sim = simKeyCompare.CosineCompare(t1.trim(),t2.trim());
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

    /**
     * 向量比较返回相似度
     */
    public float calSimilarity1(Vector[] sentence1, Vector[] sentence2) {
        float temp = 0;
        float sum_sim = 0;
        for (Vector t1 : sentence1) {
            float max_sim = 0;
            for (Vector t2 : sentence2) {
                float sim = simKeyCompare.CosineCompare1(t1,t2);
                if (sim > max_sim) {
                    max_sim = sim;
                }
            }
            sum_sim += max_sim;
        }
        return temp = sum_sim/sentence1.length;
    }

    /**
     * 问题和知识库计算相似度
     * 获取最匹配的*权重
     */
    public float calSimilarity1(Vector[] sentence1, Vector[] sentence2, float [] weightvalues) {
        float[] weightlist = new float[sentence1.length];
        float[] maxsimlist = new float[sentence1.length];
        float temp = 0;
        float sum_sim = 0;
        for (int i = 0 ; i < sentence1.length ; i++) {
            Vector t1 = sentence1[i];
            float max_sim = 0;
            int similerindex = 0;
            //计算问题分词和知识库分词找出最相近的问题*权重
            for (int j = 0 ; j < sentence2.length ; j++) {
                float sim = simKeyCompare.CosineCompare1(t1,sentence2[j]);
                if (sim > max_sim) {
                    max_sim = sim;
                    similerindex = j;
                }
            }
            //保存权重列表
            weightlist[i] = weightvalues[similerindex];
            //保存最高相似度列表
            maxsimlist[i] = max_sim;
        }
        //权重计算
        weightlist = weightNormalization(weightlist);
        for (int i = 0 ; i < sentence1.length ; i++) {
            sum_sim += maxsimlist[i] * weightlist[i];
        }
        return temp = sum_sim;
    }

    /**
     * 知识库和问题计算相似度
     * 获取最匹配的*权重
     */
    public float calSimilarity2(Vector[] sentence1, Vector[] sentence2, float [] weightvalues) {
        //权重计算
        float[] resultweight = weightNormalization(weightvalues);
        float temp = 0;
        float sum_sim = 0;
        for (int i = 0 ; i < sentence1.length ; i++) {
            Vector t1 = sentence1[i];
            float max_sim = 0;
            //计算问题分词和知识库分词找出最相近的问题*权重
            for (int j = 0 ; j < sentence2.length ; j++) {
                float sim = simKeyCompare.CosineCompare1(t1,sentence2[j]);
                if (sim > max_sim) {
                    max_sim = sim;
                }
            }
            sum_sim += max_sim * resultweight[i];
        }
        return temp = sum_sim;
    }

    /**
     * 【测试用例方法】
     * 问题和知识库计算相似度
     * 获取最匹配的*权重
     */
    public float calSimilarity3(Vector[] sentence1, Vector[] sentence2, float [] weightvalues, String[] qustionc, String[] knowledge) {
        float[] weightlist = new float[sentence1.length];
        float[] maxsimlist = new float[sentence1.length];
        int[] indexlist = new int[sentence1.length];
        float temp = 0;
        float sum_sim = 0;
        for (int i = 0 ; i < sentence1.length ; i++) {
            Vector t1 = sentence1[i];
            float max_sim = 0;
            int similerindex = 0;
            //计算问题分词和知识库分词找出最相近的问题*权重
            for (int j = 0 ; j < sentence2.length ; j++) {
                float sim = simKeyCompare.CosineCompare1(t1,sentence2[j]);
                if (sim > max_sim) {
                    max_sim = sim;
                    similerindex = j;
                }
            }
            //保存权重列表
            weightlist[i] = weightvalues[similerindex];
            //保存最高相似度列表
            maxsimlist[i] = max_sim;
            //保存知识库权重索引列表
            indexlist[i] = similerindex;
        }
        //权重计算
        weightlist = weightNormalization(weightlist);
        //正算打印对应问题分词的最高相似度 ， 对应的权重下标，权重直
        String w = StringUtils.join(weightvalues,",");
        System.out.println("正算权重直列表："+ w);//权重值列表
        for(int i = 0 ; i < sentence1.length ; i++){
            System.out.println("正算第"+(i+1)+"个词  "+qustionc[i]+"  和知识库第"+(indexlist[i]+1)+"个词  "+ knowledge[indexlist[i]] +"  最高匹配，相似度为"+maxsimlist[i]+"权重索引"+indexlist[i] + "权重为"+weightlist[i]+"乘积为"+maxsimlist[i] * weightlist[i]);
        }
        for (int i = 0 ; i < sentence1.length ; i++) {
            sum_sim += maxsimlist[i] * weightlist[i];
        }
        return temp = sum_sim;
    }

    /**
     * 【测试用例方法】
     * 知识库和问题计算相似度
     * 获取最匹配的*权重
     */
    public float calSimilarity4(Vector[] sentence1, Vector[] sentence2, float [] weightvalues, String[] qustionc, String[] knowledge) {
        int[] indexlist = new int[sentence1.length];
        float[] maxsimlist = new float[sentence1.length];
        //权重计算
        float[] resultweight = weightNormalization(weightvalues);
        float temp = 0;
        float sum_sim = 0;
        for (int i = 0 ; i < sentence1.length ; i++) {
            Vector t1 = sentence1[i];
            float max_sim = 0;
            int similerindex = 0;
            //计算问题分词和知识库分词找出最相近的问题*权重
            for (int j = 0 ; j < sentence2.length ; j++) {
                float sim = simKeyCompare.CosineCompare1(t1,sentence2[j]);
                if (sim > max_sim) {
                    max_sim = sim;
                    similerindex=j;
                }
            }
            //保存知识库权重索引列表
            indexlist[i] = similerindex;
            maxsimlist[i] = max_sim;
            sum_sim += max_sim * resultweight[i];
        }
        //反算打印对应问题分词的最高相似度 ， 对应的权重下标，权重直
        for(int i = 0 ; i < sentence1.length ; i++){
            System.out.println("反算第"+(i+1)+"个词  "+knowledge[i]+"  和问题第"+(indexlist[i]+1)+"个词  "+qustionc[indexlist[i]]+"  最高匹配，相似度为"+maxsimlist[i]+"权重索引"+indexlist[i] + "权重为"+resultweight[i]+"乘积为"+maxsimlist[i] * resultweight[i]);
        }
        return temp = sum_sim;
    }

    /**
     * 权重比列计算
     */
    public float [] weightNormalization(float [] weightvalues){
        float[] result = new float[weightvalues.length];
        float valuetotal = 0.0f;
        for(float weightvalue : weightvalues ){
            valuetotal += weightvalue;
        }
        for(int i = 0 ; i < weightvalues.length ; i++){
            result[i] = weightvalues[i]/valuetotal;
        }
        return result;
    }
}
