package com.zh.wechat.ltp.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.zh.wechat.ltp.common.base.BaseService;
import com.zh.wechat.ltp.mapper.KnowledgeMapper;
import com.zh.wechat.ltp.model.Knowledge;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
public class KnowledgeService extends BaseService<KnowledgeMapper, Knowledge> {

    LoadingCache<String, List<Knowledge>> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(600L, TimeUnit.MINUTES).build(new CacheLoader<String, List<Knowledge>>() {
                @Override
                public List<Knowledge> load(String s) throws Exception {
                    return mapper.selectListQuestion();
                }
            });

    public List<Knowledge> getCache(){
        return cache.getUnchecked("1");
    }


    public List<Knowledge> selectListQuestion(){
        return super.mapper.selectListQuestion();
    }

    public Integer selectAllNotDel(){
        return super.mapper.selectAllNotDel();
    }

}
