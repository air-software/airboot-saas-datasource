package com.airboot.project.tool.gen.service;

import com.airboot.project.tool.gen.model.entity.GenTableColumn;

import java.util.List;

/**
 * 业务字段 服务层
 *
 * @author airboot
 */
public interface IGenTableColumnService {
    
    /**
     * 查询业务字段列表
     *
     * @param tableId 业务字段编号
     * @return 业务字段集合
     */
    List<GenTableColumn> getListByTableId(Long tableId);
    
}
