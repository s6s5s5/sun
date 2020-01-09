package com.zh.wechat.ltp.model;

import com.zh.wechat.ltp.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class KnowledgeSimilar extends BaseEntity {

    /**
     * 知识库ID
     */
    private String knowledgeId;
    /**
     * 相似度
     */
    private float similar;
    /**
     * 问题分词结果
     */
    private String questionWords;
    /**
     * 答案分词结果
     */
    private String answerWords;

}
