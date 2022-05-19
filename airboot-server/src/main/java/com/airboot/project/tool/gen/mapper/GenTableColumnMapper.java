package com.airboot.project.tool.gen.mapper;

import com.airboot.common.component.MyBaseMapper;
import com.airboot.project.tool.gen.model.entity.GenTableColumn;
import com.baomidou.dynamic.datasource.annotation.DS;

import java.util.List;

/**
 * 业务字段 数据层
 *
 * @author airboot
 */
@DS("common")
public interface GenTableColumnMapper extends MyBaseMapper<GenTableColumn> {
    
    /**
     * 根据表名称查询列信息
     *
     * @param tableName 表名称
     * @return 列信息
     */
    List<GenTableColumn> findDbTableColumnsByName(String tableName);
    
    /**
     * 查询业务字段列表
     *
     * @param tableId 业务字段编号
     * @return 业务字段集合
     */
    List<GenTableColumn> findListByTableId(Long tableId);
    
    /**
     * 批量删除业务字段
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteByTableIds(List<Long> ids);
}
