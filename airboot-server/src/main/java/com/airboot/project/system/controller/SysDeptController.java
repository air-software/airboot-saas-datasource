package com.airboot.project.system.controller;

import com.airboot.common.component.BaseController;
import com.airboot.common.core.aspectj.lang.annotation.Log;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.common.model.enums.StatusEnum;
import com.airboot.common.model.vo.AjaxResult;
import com.airboot.common.security.annotation.PreAuthorize;
import com.airboot.project.monitor.model.enums.OperationTypeEnum;
import com.airboot.project.system.model.entity.SysDept;
import com.airboot.project.system.model.vo.SearchSysDeptVO;
import com.airboot.project.system.service.ISysDeptService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门信息
 *
 * @author airboot
 */
@RestController
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {
    
    @Resource
    private ISysDeptService deptService;
    
    /**
     * 获取部门列表
     */
    @PreAuthorize("system:dept:list")
    @GetMapping("/list")
    public AjaxResult list(SearchSysDeptVO search) {
        List<SysDept> depts = deptService.getList(search);
        return success(depts);
    }
    
    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize("system:dept:list")
    @GetMapping("/list/exclude/{deptId}")
    public AjaxResult excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        List<SysDept> depts = deptService.getList(new SearchSysDeptVO());
        depts.removeIf(d -> d.getId().equals(deptId)
            || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + ""));
        return success(depts);
    }
    
    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize("system:dept:query")
    @GetMapping(value = "/{deptId}")
    public AjaxResult getInfo(@PathVariable Long deptId) {
        return success(deptService.getById(deptId));
    }
    
    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SearchSysDeptVO search) {
        List<SysDept> depts = deptService.getList(search);
        return success(deptService.buildTreeSelect(depts));
    }
    
    /**
     * 加载对应角色部门列表树
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    public AjaxResult roleDeptTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysDept> depts = deptService.getList(new SearchSysDeptVO());
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("checkedKeys", deptService.getListByRoleId(roleId));
        dataMap.put("depts", deptService.buildTreeSelect(depts));
        return success(dataMap);
    }
    
    /**
     * 新增部门
     */
    @PreAuthorize("system:dept:add")
    @Log(title = "部门管理", operationType = OperationTypeEnum.新增)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDept dept) {
        if (!deptService.checkDeptNameUnique(dept)) {
            return fail("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        return toAjax(deptService.save(dept));
    }
    
    /**
     * 修改部门
     */
    @PreAuthorize("system:dept:edit")
    @Log(title = "部门管理", operationType = OperationTypeEnum.修改)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDept dept) {
        if (!deptService.checkDeptNameUnique(dept)) {
            return fail("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(dept.getId())) {
            return fail("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StatusEnum.停用.equals(dept.getStatus())
                && deptService.getNormalChildrenDeptById(dept.getId()) > 0) {
            return fail("该部门包含未停用的子部门！");
        }
        return toAjax(deptService.update(dept));
    }
    
    /**
     * 删除部门
     */
    @PreAuthorize("system:dept:remove")
    @Log(title = "部门管理", operationType = OperationTypeEnum.删除)
    @DeleteMapping("/{deptId}")
    public AjaxResult remove(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return fail("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return fail("部门存在用户,不允许删除");
        }
        return toAjax(deptService.deleteById(deptId));
    }
}
