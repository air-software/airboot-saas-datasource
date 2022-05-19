package com.airboot.project.system.service;

import com.airboot.common.model.entity.TreeSelect;
import com.airboot.project.system.model.entity.SysMenu;
import com.airboot.project.system.model.vo.RouterVO;
import com.airboot.project.system.model.vo.SearchSysMenuVO;

import java.util.List;
import java.util.Set;

/**
 * 菜单 业务层
 *
 * @author airboot
 */
public interface ISysMenuService {
    
    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> getList(Long userId);
    
    /**
     * 根据用户查询系统菜单列表
     *
     * @param search 查询条件
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> getList(SearchSysMenuVO search, Long userId);
    
    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> getMenuPermsByUserId(Long userId);
    
    /**
     * 根据用户ID查询菜单树信息
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> getMenuTreeByUserId(Long userId);
    
    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    List<Long> getIdListByRoleId(Long roleId);
    
    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    List<RouterVO> buildMenus(List<SysMenu> menus);
    
    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    List<SysMenu> buildMenuTree(List<SysMenu> menus);
    
    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus);
    
    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    SysMenu getById(Long menuId);
    
    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    boolean hasChildByMenuId(Long menuId);
    
    /**
     * 查询菜单是否存在角色
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    boolean checkMenuExistRole(Long menuId);
    
    /**
     * 保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    void saveOrUpdate(SysMenu menu);
    
    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    int deleteById(Long menuId);
    
    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    boolean checkMenuNameUnique(SysMenu menu);
}
