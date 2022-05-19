package com.airboot.project.system.controller;

import com.airboot.common.component.BaseController;
import com.airboot.common.core.aspectj.lang.annotation.Log;
import com.airboot.common.model.vo.AjaxResult;
import com.airboot.common.security.annotation.PreAuthorize;
import com.airboot.project.monitor.model.enums.OperationTypeEnum;
import com.airboot.project.system.model.entity.SysNotice;
import com.airboot.project.system.model.vo.SearchSysNoticeVO;
import com.airboot.project.system.service.ISysNoticeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * 公告 信息操作处理
 *
 * @author airboot
 */
@RestController
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController {
    
    @Resource
    private ISysNoticeService noticeService;
    
    /**
     * 获取通知公告列表
     */
    @PreAuthorize("system:notice:page")
    @GetMapping("/page")
    public AjaxResult page(SearchSysNoticeVO search) {
        IPage<SysNotice> page = noticeService.getPage(search);
        return success(page);
    }
    
    /**
     * 根据通知公告编号获取详细信息
     */
    @PreAuthorize("system:notice:query")
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable Long noticeId) {
        return success(noticeService.getById(noticeId));
    }
    
    /**
     * 新增通知公告
     */
    @PreAuthorize("system:notice:add")
    @Log(title = "通知公告", operationType = OperationTypeEnum.新增)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysNotice notice) {
        noticeService.saveOrUpdate(notice);
        return success();
    }
    
    /**
     * 修改通知公告
     */
    @PreAuthorize("system:notice:edit")
    @Log(title = "通知公告", operationType = OperationTypeEnum.修改)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysNotice notice) {
        noticeService.saveOrUpdate(notice);
        return success();
    }
    
    /**
     * 删除通知公告
     */
    @PreAuthorize("system:notice:remove")
    @Log(title = "通知公告", operationType = OperationTypeEnum.删除)
    @DeleteMapping("/{noticeIds}")
    public AjaxResult remove(@PathVariable Long[] noticeIds) {
        return toAjax(noticeService.deleteByIds(Arrays.asList(noticeIds)));
    }
}
