package com.airboot.project.system.controller;

import com.airboot.common.component.BaseController;
import com.airboot.common.core.aspectj.lang.annotation.Log;
import com.airboot.common.model.vo.AjaxResult;
import com.airboot.common.security.annotation.PreAuthorize;
import com.airboot.project.monitor.model.enums.OperationTypeEnum;
import com.airboot.project.system.model.entity.SysDataSource;
import com.airboot.project.system.model.vo.SearchSysDataSourceVO;
import com.airboot.project.system.service.ISysDataSourceService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 数据源Controller
 * 
 * @author airboot
 */
@Slf4j
@RestController
@RequestMapping("/system/datasource")
public class SysDataSourceController extends BaseController {

    @Resource
    private ISysDataSourceService sysDataSourceService;

    /**
     * 查询数据源分页
     */
    @PreAuthorize("system:datasource:page")
    @GetMapping("/page")
    public AjaxResult page(SearchSysDataSourceVO search) {
        IPage<SysDataSource> page = sysDataSourceService.getPage(search);
        return success(page);
    }

    /**
     * 查询数据源列表
     */
    @PreAuthorize("system:datasource:page")
    @GetMapping("/list")
    public AjaxResult list(SearchSysDataSourceVO search) {
        List<SysDataSource> list = sysDataSourceService.getList(search);
        return success(list);
    }

    /**
     * 获取数据源详细信息
     */
    @PreAuthorize("system:datasource:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(sysDataSourceService.getById(id));
    }

    /**
     * 新增数据源
     */
    @PreAuthorize("system:datasource:add")
    @Log(title = "数据源", operationType = OperationTypeEnum.新增)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDataSource sysDataSource) {
        sysDataSourceService.saveOrUpdate(sysDataSource);
        return success();
    }

    /**
     * 修改数据源
     */
    @PreAuthorize("system:datasource:edit")
    @Log(title = "数据源", operationType = OperationTypeEnum.修改)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDataSource sysDataSource) {
        sysDataSourceService.saveOrUpdate(sysDataSource);
        return success();
    }

    /**
     * 删除数据源
     */
    @PreAuthorize("system:datasource:remove")
    @Log(title = "数据源", operationType = OperationTypeEnum.删除)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysDataSourceService.deleteByIds(Arrays.asList(ids)));
    }
    
}
