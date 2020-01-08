package com.zh.wechat.ltp.controller;

import com.alibaba.fastjson.JSON;
import com.zh.wechat.ltp.model.Knowledge;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.zh.wechat.ltp.common.JobInit.*;

@RestController
@RequestMapping("/wechat-ai")
@Log4j2
public class InitKnowledgeController {
    @Value("${MyYml.ModelPath}")
    private String modelPath;
    @Value("${MyYml.KeywordPath}")
    private String keywordPath;

    /**
     * 获取所有知识-分词
     * @param
     * @return
     */
/*    @RequestMapping(value = "/postKnowledge",produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
    public String segmentorKnowledge(@RequestBody String knowledgeList){
        List<Knowledge> knowledges= JSON.parseArray(knowledgeList,Knowledge.class);
        saveSegmentorKnowledge(knowledges);
        return String.valueOf(knowledges.size());
    }*/

    /**
     * 静态存储分词结果
     */
/*    public void saveSegmentorKnowledge(List<Knowledge> knowledgeList){
        log.info(System.getProperty("java.library.path"));
        log.info("启动分词时加载动态库文件：ltp4j-0.1.0-SNAPSHOT.so");
        log.info("初始化静态存储分词结果，加载知识库中并分词保存中...");
        try {

            int row=0;
            for (Knowledge knowledge : knowledgeList) {
                try {
                    if(!knowledge.getQuestionToCalculate().equals("null")&&!knowledge.equals("null")){
                        *//**
                         * 存储分词后的结果
                         *//*
                        List<String> termList = new ArrayList<String>();
                        int size = segmentor.segment(knowledge.getQuestionToCalculate(),termList);
                        vec[row] = termList.toString();
                        vec_id[row] = knowledge.getId();
                        row++;
                    }
                    knowledge_num = row;
                }
                catch (Exception e){
                    //打印异常
                    log.error("error in here:{},question{}", knowledge.getId(),knowledge.getQuestion());
                }
            }
            log.info("知识库分词结束，分词条数:{}",row);
        } catch (Exception e) {
            //打印异常
            log.error("JobInit.createIndex_w.error{}", e);
        }
    }*/
}
