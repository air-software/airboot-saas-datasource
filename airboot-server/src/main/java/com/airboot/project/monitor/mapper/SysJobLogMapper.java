package com.airboot.project.monitor.mapper;

import com.airboot.common.component.MyBaseMapper;
import com.airboot.project.monitor.model.entity.SysJobLog;
import com.airboot.project.monitor.model.vo.SearchSysJobLogVO;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 调度任务日志信息 数据层
 *
 * @author airboot
 */
@DS("common")
public interface SysJobLogMapper extends MyBaseMapper<SysJobLog> {
    
    /**
     * 查询调度任务日志分页
     *
     * @param search 查询条件
     * @return 操作日志集合
     */
    IPage<SysJobLog> findPage(SearchSysJobLogVO search);
    
    /**
     * 查询调度任务日志
     *
     * @param search 查询条件
     * @return 调度任务日志集合
     */
    List<SysJobLog> findList(SearchSysJobLogVO search);
    
    /**
     * 清空任务日志
     */
    void cleanJobLog();
}
