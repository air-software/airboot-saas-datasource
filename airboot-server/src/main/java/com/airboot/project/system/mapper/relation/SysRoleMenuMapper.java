package com.airboot.project.system.mapper.relation;

import com.airboot.common.component.MyBaseMapper;
import com.airboot.project.system.model.entity.relation.SysRoleMenu;

import java.util.List;

/**
 * 角色与菜单关联表 数据层
 *
 * @author airboot
 */
public interface SysRoleMenuMapper extends MyBaseMapper<SysRoleMenu> {
    
    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    int checkMenuExistRole(Long menuId);
    
    /**
     * 通过角色ID删除角色和菜单关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    int deleteByRoleId(Long roleId);
    
    /**
     * 批量新增角色菜单信息
     *
     * @param roleMenuList 角色菜单列表
     * @return 结果
     */
    int batchInsert(List<SysRoleMenu> roleMenuList);
    
    /**
     * 获取用户拥有权限的菜单ID集合
     */
    List<Long> findMenuIdListByUserId(Long userId);
    
    /**
     * 获取角色拥有权限的菜单ID集合
     */
    List<Long> findMenuIdListByRoleId(Long roleId);
    
    /**
     * 获取用户拥有权限的菜单ID集合，相关角色为正常状态
     */
    List<Long> findMenuIdListByUserIdAndNormalRole(Long userId);
    
    /**
     * 获取角色拥有权限的菜单ID集合，相关角色为正常状态
     */
    List<Long> findMenuIdListByNormalRoleId(Long roleId);
    
}
