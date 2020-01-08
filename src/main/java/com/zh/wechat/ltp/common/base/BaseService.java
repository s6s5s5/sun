package com.zh.wechat.ltp.common.base;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zh.wechat.ltp.common.util.EntityUtils;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public abstract class BaseService<M extends Mapper<T>, T extends BaseEntity>{
    @Autowired
    protected M mapper;

    public void setMapper(M mapper) {
        this.mapper = mapper;
    }

    public T selectOne(T entity) {
        EntityUtils.setDelFlag(entity);
        return mapper.selectOne(entity);
    }

    public T selectById(Object id) {
        return mapper.selectByPrimaryKey(id);
    }

    public List<T> selectList(T entity) {
        EntityUtils.setDelFlag(entity);
        return mapper.select(entity);
    }

    public List<T> selectListAll() {
        return mapper.selectAll();
    }

    public Page<T> selectByQuery(T entity) {
        EntityUtils.setDelFlag(entity);
        Page<T> result;
        if (Strings.isNullOrEmpty(entity.getOrderBy())) {
            result = PageHelper.startPage(entity.getCurrentPage(), entity.getSize());
        } else {
            result = PageHelper.startPage(entity.getCurrentPage(), entity.getSize(), entity.getOrderBy());
        }
        mapper.select(entity);
        return result;
    }

    public Long selectCount(T entity) {
        EntityUtils.setDelFlag(entity);
        return new Long(mapper.selectCount(entity));
    }

    public void insert(T entity) {
        //EntityUtils.setCreatAndUpdatInfo(entity);
        mapper.insert(entity);
    }

    public void insertSelective(T entity) {
        mapper.insertSelective(entity);
    }

    public void delete(T entity) {
        mapper.delete(entity);
    }

    public void deleteById(Object id) {
        mapper.deleteByPrimaryKey(id);
    }

    public void updateById(T entity) {
        //EntityUtils.setUpdatedInfo(entity);
        mapper.updateByPrimaryKey(entity);
    }

    public void updateSelectiveById(T entity) {
        //EntityUtils.setUpdatedInfo(entity);
        mapper.updateByPrimaryKeySelective(entity);
    }

    public void updateByExample(T entity, Object example){
        //EntityUtils.setUpdatedInfo(entity);
        mapper.updateByExample(entity, example);
    }

    public void updateSelectiveByExample(T entity, Object example){
        //EntityUtils.setUpdatedInfo(entity);
        mapper.updateByExampleSelective(entity, example);
    }

    public List<T> selectByExample(Object example) {
        return mapper.selectByExample(example);
    }

    public int selectCountByExample(Object example) {
        return mapper.selectCountByExample(example);
    }
}
