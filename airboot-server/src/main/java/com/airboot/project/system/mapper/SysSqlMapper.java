package com.airboot.project.system.mapper;

import com.airboot.common.component.MyBaseMapper;
import com.airboot.common.model.entity.BaseEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 执行SQL接口
 * 
 * @author airboot
 */
public interface SysSqlMapper extends MyBaseMapper<BaseEntity> {
    
    /**
     * 创建租户Schema
     */
    void createSchema(@Param("schemaName") String schemaName);
    
    /**
     * 执行SQL语句
     */
    int executeSql(@Param("sqlStr") String sqlStr);

}
