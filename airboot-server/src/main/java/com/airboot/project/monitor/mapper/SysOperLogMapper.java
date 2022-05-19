package com.airboot.project.monitor.mapper;

import com.airboot.common.component.MyBaseMapper;
import com.airboot.project.monitor.model.entity.SysOperLog;
import com.airboot.project.monitor.model.vo.SearchSysOperLogVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 操作日志 数据层
 *
 * @author airboot
 */
public interface SysOperLogMapper extends MyBaseMapper<SysOperLog> {
    
    /**
     * 查询系统操作日志分页
     *
     * @param search 查询条件
     * @return 操作日志分页
     */
    IPage<SysOperLog> findPage(SearchSysOperLogVO search);
    
    /**
     * 查询系统操作日志集合
     *
     * @param search 查询条件
     * @return 操作日志集合
     */
    List<SysOperLog> findList(SearchSysOperLogVO search);
    
    /**
     * 清空操作日志
     */
    void cleanOperLog();
}
