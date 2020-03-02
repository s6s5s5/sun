package com.zh.wechat.ltp.common;

import com.zh.wechat.ltp.model.Knowledge;
import com.zh.wechat.ltp.service.KnowledgeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author LiJun
 * @version 1.0
 * @date 2019/10/9 10:09
 */
@Component
@EnableScheduling
@Log4j2
public class ScheduledJob {

    @Resource
    JobInit jobInit;

    @Resource
    KnowledgeService knowledgeService;

    @Scheduled(cron = "0 0 0/12 * * *")
    public void InitJob(){
        try {
            Knowledge knowledge = new Knowledge();
            knowledge.setDelFlag(Short.valueOf("0"));
            List<Knowledge> knowledgeList = knowledgeService.selectList(knowledge);
            log.info("当前静态分词结果行数：{} ,数据库中现有知识条数：{}",JobInit.knowledge_num,knowledgeList.size());
            if(JobInit.knowledge_num != knowledgeList.size()){
                log.info("开始重新分词存储");
                jobInit.SegmentorKnowledge();
                log.info("重新分词存储结束");
            }
        } catch (Exception e){
            log.error("重构索引失败{}", e);
        }
    }


}
