package com.airboot.project.monitor.controller;

import com.airboot.common.component.BaseController;
import com.airboot.common.core.aspectj.lang.annotation.Log;
import com.airboot.common.core.utils.poi.ExcelUtil;
import com.airboot.common.model.vo.AjaxResult;
import com.airboot.common.security.annotation.PreAuthorize;
import com.airboot.project.monitor.model.entity.SysJobLog;
import com.airboot.project.monitor.model.enums.OperationTypeEnum;
import com.airboot.project.monitor.model.vo.SearchSysJobLogVO;
import com.airboot.project.monitor.service.ISysJobLogService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 调度日志操作处理
 *
 * @author airboot
 */
@DS("common")
@RestController
@RequestMapping("/monitor/jobLog")
public class SysJobLogController extends BaseController {
    
    @Resource
    private ISysJobLogService jobLogService;
    
    /**
     * 查询定时任务调度日志列表
     */
    @PreAuthorize("monitor:job:page")
    @GetMapping("/page")
    public AjaxResult page(SearchSysJobLogVO search) {
        IPage<SysJobLog> page = jobLogService.getPage(search);
        return success(page);
    }
    
    /**
     * 导出定时任务调度日志列表
     */
    @PreAuthorize("monitor:job:export")
    @Log(title = "定时任务调度日志", operationType = OperationTypeEnum.导出)
    @GetMapping("/export")
    public AjaxResult export(SearchSysJobLogVO search) {
        List<SysJobLog> list = jobLogService.getList(search);
        ExcelUtil<SysJobLog> util = new ExcelUtil<>(SysJobLog.class);
        return util.exportExcel(list, "调度日志");
    }
    
    /**
     * 根据调度编号获取详细信息
     */
    @PreAuthorize("monitor:job:query")
    @GetMapping(value = "/{jobLogId}")
    public AjaxResult getInfo(@PathVariable Long jobLogId) {
        return success(jobLogService.getById(jobLogId));
    }
    
    
    /**
     * 删除定时任务调度日志
     */
    @PreAuthorize("monitor:job:remove")
    @Log(title = "定时任务调度日志", operationType = OperationTypeEnum.删除)
    @DeleteMapping("/{jobLogIds}")
    public AjaxResult remove(@PathVariable Long[] jobLogIds) {
        return toAjax(jobLogService.deleteByIds(Arrays.asList(jobLogIds)));
    }
    
    /**
     * 清空定时任务调度日志
     */
    @PreAuthorize("monitor:job:remove")
    @Log(title = "定时任务调度日志", operationType = OperationTypeEnum.清空数据)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        jobLogService.cleanJobLog();
        return success();
    }
}
