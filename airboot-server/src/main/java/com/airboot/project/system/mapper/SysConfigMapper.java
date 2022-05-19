package com.airboot.project.system.mapper;

import com.airboot.common.component.MyBaseMapper;
import com.airboot.project.system.model.entity.SysConfig;
import com.airboot.project.system.model.vo.SearchSysConfigVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 参数配置 数据层
 *
 * @author airboot
 */
public interface SysConfigMapper extends MyBaseMapper<SysConfig> {
    
    /**
     * 查询参数配置分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    IPage<SysConfig> findPage(SearchSysConfigVO search);
    
    /**
     * 查询参数配置列表
     *
     * @param search 查询条件
     * @return 参数配置集合
     */
    List<SysConfig> findList(SearchSysConfigVO search);
    
}
