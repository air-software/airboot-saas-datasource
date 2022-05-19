package com.airboot.project.monitor.mapper;

import com.airboot.common.component.MyBaseMapper;
import com.airboot.project.monitor.model.entity.SysJob;
import com.airboot.project.monitor.model.vo.SearchSysJobVO;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 调度任务信息 数据层
 *
 * @author airboot
 */
@DS("common")
public interface SysJobMapper extends MyBaseMapper<SysJob> {
    
    /**
     * 查询调度任务分页
     *
     * @param search 调度信息
     * @return 操作日志集合
     */
    IPage<SysJob> findPage(SearchSysJobVO search);
    
    /**
     * 查询调度任务列表
     *
     * @param search
     * @return
     */
    List<SysJob> findList(SearchSysJobVO search);
    
}
