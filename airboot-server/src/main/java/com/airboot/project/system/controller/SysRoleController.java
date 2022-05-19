package com.airboot.project.system.controller;

import com.airboot.common.component.BaseController;
import com.airboot.common.core.aspectj.lang.annotation.Log;
import com.airboot.common.core.utils.poi.ExcelUtil;
import com.airboot.common.model.vo.AjaxResult;
import com.airboot.common.security.LoginUser;
import com.airboot.common.security.LoginUserContextHolder;
import com.airboot.common.security.annotation.PreAuthorize;
import com.airboot.common.security.service.SysPermissionService;
import com.airboot.common.security.service.TokenService;
import com.airboot.project.monitor.model.enums.OperationTypeEnum;
import com.airboot.project.system.model.entity.SysRole;
import com.airboot.project.system.model.vo.SearchSysRoleVO;
import com.airboot.project.system.service.ISysRoleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色信息
 *
 * @author airboot
 */
@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {
    
    @Resource
    private ISysRoleService roleService;
    
    @Resource
    private TokenService tokenService;
    
    @Resource
    private SysPermissionService permissionService;
    
    @PreAuthorize("system:role:page")
    @GetMapping("/page")
    public AjaxResult page(SearchSysRoleVO search) {
        IPage<SysRole> page = roleService.getPage(search);
        return success(page);
    }
    
    @Log(title = "角色管理", operationType = OperationTypeEnum.导出)
    @PreAuthorize("system:role:export")
    @GetMapping("/export")
    public AjaxResult export(SearchSysRoleVO search) {
        List<SysRole> list = roleService.getList(search);
        ExcelUtil<SysRole> util = new ExcelUtil<>(SysRole.class);
        return util.exportExcel(list, "角色数据");
    }
    
    /**
     * 根据角色编号获取详细信息
     */
    @PreAuthorize("system:role:query")
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(@PathVariable Long roleId) {
        return success(roleService.getById(roleId));
    }
    
    /**
     * 新增角色
     */
    @PreAuthorize("system:role:add")
    @Log(title = "角色管理", operationType = OperationTypeEnum.新增)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysRole role) {
        if (!roleService.checkRoleNameUnique(role)) {
            return fail("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return fail("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        return toAjax(roleService.save(role));
        
    }
    
    /**
     * 修改保存角色
     */
    @PreAuthorize("system:role:edit")
    @Log(title = "角色管理", operationType = OperationTypeEnum.修改)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        if (!roleService.checkRoleNameUnique(role)) {
            return fail("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return fail("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        
        int result = roleService.update(role);
        LoginUser loginUser = LoginUserContextHolder.getLoginUser();
        if (!loginUser.isTenantAdmin() && result > 0) {
            // 如果修改的角色是当前登录用户所属角色，则更新缓存中的用户权限
            for (SysRole userRole : loginUser.getUser().getRoles()) {
                if (userRole.getId().equals(role.getId())) {
                    loginUser.setPermissions(permissionService.getMenuPermission(loginUser.getUser()));
                    tokenService.setLoginUser(loginUser);
                    break;
                }
            }
        }
        
        return toAjax(result);
    }
    
    /**
     * 修改保存数据权限
     */
    @PreAuthorize("system:role:edit")
    @Log(title = "角色管理", operationType = OperationTypeEnum.修改)
    @PutMapping("/dataScope")
    public AjaxResult dataScope(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        return toAjax(roleService.authDataScope(role));
    }
    
    /**
     * 状态修改
     */
    @PreAuthorize("system:role:edit")
    @Log(title = "角色管理", operationType = OperationTypeEnum.修改)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        // 新建一个role实例来更新，防止更新到其他字段
        roleService.updateStatus(SysRole.builder()
            .id(role.getId())
            .status(role.getStatus())
            .build());
        return success();
    }
    
    /**
     * 删除角色
     */
    @PreAuthorize("system:role:remove")
    @Log(title = "角色管理", operationType = OperationTypeEnum.删除)
    @DeleteMapping("/{roleIds}")
    public AjaxResult remove(@PathVariable Long[] roleIds) {
        return toAjax(roleService.deleteByIds(roleIds));
    }
    
    /**
     * 获取角色选择框列表
     */
    @PreAuthorize("system:role:query")
    @GetMapping("/optionselect")
    public AjaxResult optionselect() {
        return success(roleService.getAll());
    }
}
