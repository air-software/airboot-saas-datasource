package com.airboot.project.system.mapper;


import com.airboot.common.component.MyBaseMapper;
import com.airboot.project.system.model.entity.SysDataSource;
import com.airboot.project.system.model.vo.SearchSysDataSourceVO;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 数据源Mapper接口
 * 
 * @author airboot
 */
@DS("common")
public interface SysDataSourceMapper extends MyBaseMapper<SysDataSource> {
    
    /**
     * 查询数据源分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    IPage<SysDataSource> findPage(SearchSysDataSourceVO search);
    
    /**
     * 查询数据源列表
     *
     * @param search 查询条件
     * @return 数据源集合
     */
    List<SysDataSource> findList(SearchSysDataSourceVO search);

}
