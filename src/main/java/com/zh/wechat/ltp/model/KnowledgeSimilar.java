package com.zh.wechat.ltp.model;

import com.zh.wechat.ltp.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class KnowledgeSimilar extends BaseEntity implements Comparable<KnowledgeSimilar>{

    public KnowledgeSimilar(String knowledgeId,String SeparateWords,float similar){
        this.similar = similar;
        this.SeparateWords = SeparateWords;
        this.knowledgeId = knowledgeId;
    }

    /**
     * 知识库ID
     */
    private String knowledgeId;
    /**
     * 相似度
     */
    private float similar;
    /**
     * 分词结果
     */
    private String SeparateWords;


    @Override
    public int compareTo(KnowledgeSimilar knowledgeSimilar){
        BigDecimal b1 = BigDecimal.valueOf(this.similar);
        BigDecimal b2 = BigDecimal.valueOf(knowledgeSimilar.getSimilar());
        return b2.intValue()-b1.intValue();
    }

}
