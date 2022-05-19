package com.airboot.project.system.controller;

import com.airboot.common.component.BaseController;
import com.airboot.common.core.aspectj.lang.annotation.Log;
import com.airboot.common.core.utils.poi.ExcelUtil;
import com.airboot.common.model.vo.AjaxResult;
import com.airboot.common.security.annotation.PreAuthorize;
import com.airboot.project.monitor.model.enums.OperationTypeEnum;
import com.airboot.project.system.model.entity.SysConfig;
import com.airboot.project.system.model.vo.SearchSysConfigVO;
import com.airboot.project.system.service.ISysConfigService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 参数配置 信息操作处理
 *
 * @author airboot
 */
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {
    
    @Resource
    private ISysConfigService configService;
    
    /**
     * 获取参数配置列表
     */
    @PreAuthorize("system:config:page")
    @GetMapping("/page")
    public AjaxResult page(SearchSysConfigVO search) {
        IPage<SysConfig> page = configService.getPage(search);
        return success(page);
    }
    
    @Log(title = "参数管理", operationType = OperationTypeEnum.导出)
    @PreAuthorize("system:config:export")
    @GetMapping("/export")
    public AjaxResult export(SearchSysConfigVO search) {
        List<SysConfig> list = configService.getList(search);
        ExcelUtil<SysConfig> util = new ExcelUtil<>(SysConfig.class);
        return util.exportExcel(list, "参数数据");
    }
    
    /**
     * 根据参数编号获取详细信息
     */
    @PreAuthorize("system:config:query")
    @GetMapping(value = "/{configId}")
    public AjaxResult getInfo(@PathVariable Long configId) {
        return success(configService.getById(configId));
    }
    
    /**
     * 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    public AjaxResult getConfigKey(@PathVariable String configKey) {
        return success(configService.getByKey(configKey));
    }
    
    /**
     * 根据参数键名查询无需登录验证的参数值
     */
    @GetMapping(value = "/unauth-key/{configKey}")
    public AjaxResult getUnauthConfigKey(@PathVariable String configKey) {
        return success(configService.getUnauthByKey(configKey));
    }
    
    /**
     * 新增参数配置
     */
    @PreAuthorize("system:config:add")
    @Log(title = "参数管理", operationType = OperationTypeEnum.新增)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysConfig config) {
        if (!configService.checkConfigKeyUnique(config)) {
            return fail("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        configService.saveOrUpdate(config);
        return success();
    }
    
    /**
     * 修改参数配置
     */
    @PreAuthorize("system:config:edit")
    @Log(title = "参数管理", operationType = OperationTypeEnum.修改)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysConfig config) {
        if (!configService.checkConfigKeyUnique(config)) {
            return fail("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        configService.saveOrUpdate(config);
        return success();
    }
    
    /**
     * 删除参数配置
     */
    @PreAuthorize("system:config:remove")
    @Log(title = "参数管理", operationType = OperationTypeEnum.删除)
    @DeleteMapping("/{configIds}")
    public AjaxResult remove(@PathVariable Long[] configIds) {
        return toAjax(configService.deleteByIds(Arrays.asList(configIds)));
    }
}
