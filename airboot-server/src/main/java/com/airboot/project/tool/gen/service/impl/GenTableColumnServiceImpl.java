package com.airboot.project.tool.gen.service.impl;

import com.airboot.project.tool.gen.mapper.GenTableColumnMapper;
import com.airboot.project.tool.gen.model.entity.GenTableColumn;
import com.airboot.project.tool.gen.service.IGenTableColumnService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 业务字段 服务层实现
 *
 * @author airboot
 */
@Service
public class GenTableColumnServiceImpl implements IGenTableColumnService {
    
    @Resource
    private GenTableColumnMapper genTableColumnMapper;
    
    /**
     * 查询业务字段列表
     *
     * @param tableId 业务字段编号
     * @return 业务字段集合
     */
    @Override
    public List<GenTableColumn> getListByTableId(Long tableId) {
        return genTableColumnMapper.findListByTableId(tableId);
    }
    
}
