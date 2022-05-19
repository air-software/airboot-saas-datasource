package com.airboot.common.component;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 扩展Mybatis-Plus的BaseMapper
 *
 * @author airboot
 */
@SuppressWarnings("unchecked")
public interface MyBaseMapper<T> extends BaseMapper<T> {
    
    /**
     * 判断数据库操作是否成功
     *
     * @param result 数据库操作返回影响条数
     * @return boolean
     */
    default boolean retBool(Integer result) {
        return SqlHelper.retBool(result);
    }
    
    /**
     * TableId 注解存在更新记录，否插入一条记录
     *
     * @param entity 实体对象
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    default void saveOrUpdate(T entity) {
        Assert.notNull(entity, "保存对象为空");
        
        Class<?> cls = entity.getClass();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
        Object idVal = ReflectionKit.getFieldValue(entity, tableInfo.getKeyProperty());
        
        boolean result = StringUtils.checkValNull(idVal) || Objects.isNull(this.selectById((Serializable) idVal)) ? retBool(this.insert(entity)) : retBool(this.updateById(entity));
        
        Assert.isTrue(result, "保存失败");
    }
    
    /**
     * 批量保存，保存时如已存在则更新，否则新增
     * @param entityList
     */
    @Transactional(rollbackFor = Exception.class)
    default void saveOrUpdateBatch(Collection<T> entityList) {
        try {
            for (T entity : entityList) {
                saveOrUpdate(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException("批量保存失败", e);
        }
        
    }
    
    /**
     * 获取一条数据
     * @param queryWrapper
     * @param throwEx 如果存在多条数据，是否抛出异常。若不抛出异常，则取第一条数据。
     * @return
     */
    default T getOne(Wrapper<T> queryWrapper, boolean throwEx) {
        if (throwEx) {
            return this.selectOne(queryWrapper);
        }
        return getObject(this.selectList(queryWrapper));
    }
    
    /**
     * 从list中取第一条数据返回对应List中泛型的单个结果
     *
     * @param list ignore
     * @param <E>  ignore
     * @return ignore
     */
    default <E> E getObject(List<E> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
}
