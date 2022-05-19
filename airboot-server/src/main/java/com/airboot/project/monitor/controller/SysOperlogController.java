package com.airboot.project.monitor.controller;

import com.airboot.common.component.BaseController;
import com.airboot.common.core.aspectj.lang.annotation.Log;
import com.airboot.project.monitor.model.enums.OperationTypeEnum;
import com.airboot.common.core.utils.poi.ExcelUtil;
import com.airboot.common.model.vo.AjaxResult;
import com.airboot.project.monitor.model.entity.SysOperLog;
import com.airboot.project.monitor.model.vo.SearchSysOperLogVO;
import com.airboot.project.monitor.service.ISysOperLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.airboot.common.security.annotation.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 操作日志记录
 *
 * @author airboot
 */
@RestController
@RequestMapping("/monitor/operlog")
public class SysOperlogController extends BaseController {
    
    @Resource
    private ISysOperLogService operLogService;
    
    @PreAuthorize("monitor:operlog:page")
    @GetMapping("/page")
    public AjaxResult page(SearchSysOperLogVO search) {
        IPage<SysOperLog> page = operLogService.getPage(search);
        return success(page);
    }
    
    @Log(title = "操作日志", operationType = OperationTypeEnum.导出)
    @PreAuthorize("monitor:operlog:export")
    @GetMapping("/export")
    public AjaxResult export(SearchSysOperLogVO search) {
        List<SysOperLog> list = operLogService.getList(search);
        ExcelUtil<SysOperLog> util = new ExcelUtil<>(SysOperLog.class);
        return util.exportExcel(list, "操作日志");
    }
    
    @PreAuthorize("monitor:operlog:remove")
    @DeleteMapping("/{operIds}")
    public AjaxResult remove(@PathVariable Long[] operIds) {
        return toAjax(operLogService.deleteByIds(Arrays.asList(operIds)));
    }
    
    @Log(title = "操作日志", operationType = OperationTypeEnum.清空数据)
    @PreAuthorize("monitor:operlog:remove")
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        operLogService.cleanOperLog();
        return success();
    }
}
