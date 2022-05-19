package com.airboot.project.system.controller;

import com.airboot.common.component.BaseController;
import com.airboot.common.core.aspectj.lang.annotation.Log;
import com.airboot.common.core.utils.poi.ExcelUtil;
import com.airboot.common.model.vo.AjaxResult;
import com.airboot.common.security.annotation.PreAuthorize;
import com.airboot.project.monitor.model.enums.OperationTypeEnum;
import com.airboot.project.system.model.entity.SysPost;
import com.airboot.project.system.model.vo.SearchSysPostVO;
import com.airboot.project.system.service.ISysPostService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 岗位信息操作处理
 *
 * @author airboot
 */
@RestController
@RequestMapping("/system/post")
public class SysPostController extends BaseController {
    
    @Resource
    private ISysPostService postService;
    
    /**
     * 获取岗位列表
     */
    @PreAuthorize("system:post:page")
    @GetMapping("/page")
    public AjaxResult page(SearchSysPostVO search) {
        IPage<SysPost> page = postService.getPage(search);
        return success(page);
    }
    
    @Log(title = "岗位管理", operationType = OperationTypeEnum.导出)
    @PreAuthorize("system:post:export")
    @GetMapping("/export")
    public AjaxResult export(SearchSysPostVO search) {
        List<SysPost> list = postService.getList(search);
        ExcelUtil<SysPost> util = new ExcelUtil<>(SysPost.class);
        return util.exportExcel(list, "岗位数据");
    }
    
    /**
     * 根据岗位编号获取详细信息
     */
    @PreAuthorize("system:post:query")
    @GetMapping(value = "/{postId}")
    public AjaxResult getInfo(@PathVariable Long postId) {
        return success(postService.getById(postId));
    }
    
    /**
     * 新增岗位
     */
    @PreAuthorize("system:post:add")
    @Log(title = "岗位管理", operationType = OperationTypeEnum.新增)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysPost post) {
        if (!postService.checkPostNameUnique(post)) {
            return fail("新增岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (!postService.checkPostCodeUnique(post)) {
            return fail("新增岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        postService.saveOrUpdate(post);
        return success();
    }
    
    /**
     * 修改岗位
     */
    @PreAuthorize("system:post:edit")
    @Log(title = "岗位管理", operationType = OperationTypeEnum.修改)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysPost post) {
        if (!postService.checkPostNameUnique(post)) {
            return fail("修改岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (!postService.checkPostCodeUnique(post)) {
            return fail("修改岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        postService.saveOrUpdate(post);
        return success();
    }
    
    /**
     * 删除岗位
     */
    @PreAuthorize("system:post:remove")
    @Log(title = "岗位管理", operationType = OperationTypeEnum.删除)
    @DeleteMapping("/{postIds}")
    public AjaxResult remove(@PathVariable Long[] postIds) {
        return toAjax(postService.deleteByIds(Arrays.asList(postIds)));
    }
    
    /**
     * 获取岗位选择框列表
     */
    @GetMapping("/optionselect")
    public AjaxResult optionselect() {
        List<SysPost> posts = postService.getAll();
        return success(posts);
    }
}
