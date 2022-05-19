package com.airboot.project.monitor.service;

import com.airboot.project.monitor.model.entity.SysOperLog;
import com.airboot.project.monitor.model.vo.SearchSysOperLogVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 操作日志 服务层
 *
 * @author airboot
 */
public interface ISysOperLogService {
    
    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    void save(SysOperLog operLog);
    
    /**
     * 查询系统操作日志分页
     *
     * @param search 查询条件
     * @return 操作日志分页
     */
    IPage<SysOperLog> getPage(SearchSysOperLogVO search);
    
    /**
     * 查询系统操作日志集合
     *
     * @param search 查询条件
     * @return 操作日志集合
     */
    List<SysOperLog> getList(SearchSysOperLogVO search);
    
    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    int deleteByIds(List<Long> operIds);
    
    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    SysOperLog getById(Long operId);
    
    /**
     * 清空操作日志
     */
    void cleanOperLog();
}
