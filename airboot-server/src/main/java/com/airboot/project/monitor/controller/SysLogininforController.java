package com.airboot.project.monitor.controller;

import com.airboot.common.component.BaseController;
import com.airboot.common.core.aspectj.lang.annotation.Log;
import com.airboot.project.monitor.model.enums.OperationTypeEnum;
import com.airboot.common.core.utils.poi.ExcelUtil;
import com.airboot.common.model.vo.AjaxResult;
import com.airboot.project.monitor.model.entity.SysLogininfor;
import com.airboot.project.monitor.model.vo.SearchSysLogininforVO;
import com.airboot.project.monitor.service.ISysLogininforService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.airboot.common.security.annotation.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 系统访问记录
 *
 * @author airboot
 */
@RestController
@RequestMapping("/monitor/logininfor")
public class SysLogininforController extends BaseController {
    
    @Resource
    private ISysLogininforService logininforService;
    
    @PreAuthorize("monitor:logininfor:page")
    @GetMapping("/page")
    public AjaxResult page(SearchSysLogininforVO search) {
        IPage<SysLogininfor> page = logininforService.getPage(search);
        return success(page);
    }
    
    @Log(title = "登录日志", operationType = OperationTypeEnum.导出)
    @PreAuthorize("monitor:logininfor:export")
    @GetMapping("/export")
    public AjaxResult export(SearchSysLogininforVO search) {
        List<SysLogininfor> list = logininforService.getList(search);
        ExcelUtil<SysLogininfor> util = new ExcelUtil<>(SysLogininfor.class);
        return util.exportExcel(list, "登录日志");
    }
    
    @PreAuthorize("monitor:logininfor:remove")
    @Log(title = "登录日志", operationType = OperationTypeEnum.删除)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds) {
        return toAjax(logininforService.deleteByIds(Arrays.asList(infoIds)));
    }
    
    @PreAuthorize("monitor:logininfor:remove")
    @Log(title = "登录日志", operationType = OperationTypeEnum.清空数据)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        logininforService.cleanLogininfor();
        return success();
    }
}
