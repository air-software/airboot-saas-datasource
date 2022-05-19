package com.airboot.project.system.service;

import com.airboot.project.system.model.entity.SysDataSource;
import com.airboot.project.system.model.vo.SearchSysDataSourceVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 数据源Service接口
 * 
 * @author airboot
 */
public interface ISysDataSourceService {

    /**
     * 查询数据源分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    IPage<SysDataSource> getPage(SearchSysDataSourceVO search);

    /**
     * 查询数据源列表
     * 
     * @param search 查询条件
     * @return 数据源集合
     */
    List<SysDataSource> getList(SearchSysDataSourceVO search);

    /**
     * 查询数据源
     *
     * @param id 数据源ID
     * @return 数据源
     */
    SysDataSource getById(Long id);
    
    /**
     * 修改数据源
     *
     * @param sysDataSource 数据源
     */
    void saveOrUpdate(SysDataSource sysDataSource);

    /**
     * 删除数据源信息
     *
     * @param id 数据源ID
     * @return 结果
     */
    int deleteById(Long id);

    /**
     * 批量删除数据源
     * 
     * @param idList 需要删除的数据源ID
     * @return 结果
     */
    int deleteByIds(List<Long> idList);

}
