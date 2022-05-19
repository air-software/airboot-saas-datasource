package com.airboot.project.monitor.service;

import com.airboot.project.monitor.model.entity.SysJobLog;
import com.airboot.project.monitor.model.vo.SearchSysJobLogVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 定时任务调度日志信息信息 服务层
 *
 * @author airboot
 */
public interface ISysJobLogService {
    
    /**
     * 获取quartz调度器任务日志分页
     *
     * @param search 分页查询条件
     * @return 调度任务日志集合
     */
    IPage<SysJobLog> getPage(SearchSysJobLogVO search);
    
    /**
     * 获取quartz调度器任务日志列表
     *
     * @param search 查询条件
     * @return 调度任务日志集合
     */
    List<SysJobLog> getList(SearchSysJobLogVO search);
    
    /**
     * 通过调度任务日志ID查询调度信息
     *
     * @param jobLogId 调度任务日志ID
     * @return 调度任务日志对象信息
     */
    SysJobLog getById(Long jobLogId);
    
    /**
     * 新增任务日志
     *
     * @param jobLog 调度日志信息
     */
    void save(SysJobLog jobLog);
    
    /**
     * 批量删除调度日志信息
     *
     * @param logIds 需要删除的日志ID
     * @return 结果
     */
    int deleteByIds(List<Long> logIds);
    
    /**
     * 删除任务日志
     *
     * @param jobId 调度日志ID
     * @return 结果
     */
    int deleteById(Long jobId);
    
    /**
     * 清空任务日志
     */
    void cleanJobLog();
}
