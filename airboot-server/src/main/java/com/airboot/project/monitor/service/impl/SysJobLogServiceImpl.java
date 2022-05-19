package com.airboot.project.monitor.service.impl;

import com.airboot.project.monitor.mapper.SysJobLogMapper;
import com.airboot.project.monitor.model.entity.SysJobLog;
import com.airboot.project.monitor.model.vo.SearchSysJobLogVO;
import com.airboot.project.monitor.service.ISysJobLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 定时任务调度日志信息 服务层
 *
 * @author airboot
 */
@Service
public class SysJobLogServiceImpl implements ISysJobLogService {
    
    @Resource
    private SysJobLogMapper jobLogMapper;
    
    /**
     * 获取quartz调度器任务日志分页
     *
     * @param search 分页查询条件
     * @return 调度任务日志集合
     */
    @Override
    public IPage<SysJobLog> getPage(SearchSysJobLogVO search) {
        return jobLogMapper.findPage(search);
    }
    
    /**
     * 获取quartz调度器任务日志列表
     *
     * @param search 查询条件
     * @return 调度任务日志集合
     */
    @Override
    public List<SysJobLog> getList(SearchSysJobLogVO search) {
        return jobLogMapper.findList(search);
    }
    
    /**
     * 通过调度任务日志ID查询调度信息
     *
     * @param jobLogId 调度任务日志ID
     * @return 调度任务日志对象信息
     */
    @Override
    public SysJobLog getById(Long jobLogId) {
        return jobLogMapper.selectById(jobLogId);
    }
    
    /**
     * 新增任务日志
     *
     * @param jobLog 调度日志信息
     */
    @Override
    public void save(SysJobLog jobLog) {
        jobLogMapper.insert(jobLog);
    }
    
    /**
     * 批量删除调度日志信息
     *
     * @param logIds 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteByIds(List<Long> logIds) {
        return jobLogMapper.deleteBatchIds(logIds);
    }
    
    /**
     * 删除任务日志
     *
     * @param jobId 调度日志ID
     */
    @Override
    public int deleteById(Long jobId) {
        return jobLogMapper.deleteById(jobId);
    }
    
    /**
     * 清空任务日志
     */
    @Override
    public void cleanJobLog() {
        jobLogMapper.cleanJobLog();
    }
}
