package com.zh.wechat.ltp.mapper;


import com.zh.wechat.ltp.model.Knowledge;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface KnowledgeMapper extends Mapper<Knowledge> {

    /**
     * 获取数据库，只返回ID和QUESTION
     * @author zhangLin
     * @date 2019/5/17
     * @param
     * @return java.util.List<com.customer.model.entity.Knowledge>
     */
    List<Knowledge> selectListQuestion();

    Integer selectAllNotDel();
}
