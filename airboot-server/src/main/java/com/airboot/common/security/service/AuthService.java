package com.airboot.common.security.service;

import com.airboot.common.core.utils.StringUtils;
import com.airboot.common.security.LoginUser;
import com.airboot.common.security.LoginUserContextHolder;
import com.airboot.project.system.model.entity.SysRole;
import com.airboot.project.system.model.entity.SysUser;
import com.airboot.project.system.model.enums.RoleTypeEnum;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * 自定义权限验证
 *
 * @author airboot
 */
@Service
public class AuthService {
    
    /**
     * 所有权限标识
     */
    private static final String ALL_PERMISSION = "*:*:*";
    
    private static final String PERMISSION_DELIMETER = ",";
    
    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermi(String permission) {
        if (StringUtils.isEmpty(permission)) {
            return false;
        }
        LoginUser loginUser = LoginUserContextHolder.getLoginUser();
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        return hasPermissions(loginUser.getPermissions(), permission);
    }
    
    /**
     * 验证用户是否不具备某权限，与 hasPermi逻辑相反
     *
     * @param permission 权限字符串
     * @return 用户是否不具备某权限
     */
    public boolean lacksPermi(String permission) {
        return !hasPermi(permission);
    }
    
    /**
     * 验证用户是否具有以下任意一个权限
     *
     * @param permissions 以 PERMISSION_NAMES_DELIMETER 为分隔符的权限列表
     * @return 用户是否具有以下任意一个权限
     */
    public boolean hasAnyPermi(String permissions) {
        if (StringUtils.isEmpty(permissions)) {
            return false;
        }
        LoginUser loginUser = LoginUserContextHolder.getLoginUser();
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        Set<String> authorities = loginUser.getPermissions();
        for (String permission : permissions.split(PERMISSION_DELIMETER)) {
            if (permission != null && hasPermissions(authorities, permission)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 判断是否包含权限
     *
     * @param permissions 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPermissions(Set<String> permissions, String permission) {
        return permissions.contains(ALL_PERMISSION) || permissions.contains(StringUtils.trim(permission));
    }
    
    /**
     * 判断用户是否拥有某个角色
     *
     * @param role 角色字符串
     * @return 用户是否具备某角色
     */
    public boolean hasRole(RoleTypeEnum roleType) {
        if (StringUtils.isNull(roleType)) {
            return false;
        }
        LoginUser loginUser = LoginUserContextHolder.getLoginUser();
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getUser().getRoles())) {
            return false;
        }
        for (SysRole sysRole : loginUser.getUser().getRoles()) {
            if (roleType.equals(sysRole.getRoleType())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 判断用户是否拥有某个角色
     *
     * @param user 用户
     * @param role 角色字符串
     * @return 用户是否具备某角色
     */
    public boolean hasRole(SysUser user, RoleTypeEnum roleType) {
        if (StringUtils.isNull(user) || StringUtils.isNull(roleType)) {
            return false;
        }
        for (SysRole sysRole : user.getRoles()) {
            if (roleType.equals(sysRole.getRoleType())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 验证用户是否不具备某角色，与 isRole逻辑相反。
     *
     * @param role 角色名称
     * @return 用户是否不具备某角色
     */
    public boolean lacksRole(RoleTypeEnum roleType) {
        return !hasRole(roleType);
    }
    
    /**
     * 验证用户是否具有以下任意一个角色
     *
     * @param roles 以 ROLE_NAMES_DELIMETER 为分隔符的角色列表
     * @return 用户是否具有以下任意一个角色
     */
    public boolean hasAnyRoles(List<RoleTypeEnum> roleTypeList) {
        if (CollectionUtils.isEmpty(roleTypeList)) {
            return false;
        }
        LoginUser loginUser = LoginUserContextHolder.getLoginUser();
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getUser().getRoles())) {
            return false;
        }
        for (RoleTypeEnum roleType : roleTypeList) {
            if (hasRole(roleType)) {
                return true;
            }
        }
        return false;
    }
    
}
