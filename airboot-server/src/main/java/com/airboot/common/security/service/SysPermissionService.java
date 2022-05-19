package com.airboot.common.security.service;

import com.airboot.project.system.model.entity.SysUser;
import com.airboot.project.system.service.ISysMenuService;
import com.airboot.project.system.service.ISysRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户权限处理
 *
 * @author airboot
 */
@Service
public class SysPermissionService {

    @Resource
    private ISysRoleService roleService;
    
    @Resource
    private ISysMenuService menuService;
    
    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(SysUser user) {
        Set<String> roles = new HashSet<>();
        // 超级租户管理员拥有所有权限
        if (user.isTenantAdmin()) {
            roles.add("admin");
        } else {
            roles.addAll(roleService.getPermsByUserId(user.getId()));
        }
        return roles;
    }
    
    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(SysUser user) {
        Set<String> perms = new HashSet<>();
        // 超级租户管理员拥有所有权限
        if (user.isTenantAdmin()) {
            perms.add("*:*:*");
        } else {
            perms.addAll(menuService.getMenuPermsByUserId(user.getId()));
        }
        return perms;
    }
}
