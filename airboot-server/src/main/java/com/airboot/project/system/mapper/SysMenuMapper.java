package com.airboot.project.system.mapper;

import com.airboot.common.component.MyBaseMapper;
import com.airboot.project.system.model.entity.SysMenu;
import com.airboot.project.system.model.vo.SearchSysMenuVO;
import com.baomidou.dynamic.datasource.annotation.DS;

import java.util.List;

/**
 * 菜单表 数据层
 *
 * @author airboot
 */
@DS("common")
public interface SysMenuMapper extends MyBaseMapper<SysMenu> {
    
    /**
     * 查询系统菜单列表
     *
     * @param search 查询条件
     * @return 菜单列表
     */
    List<SysMenu> findList(SearchSysMenuVO search);
    
    /**
     * 查询菜单目录
     *
     * @return 菜单目录列表
     */
    List<SysMenu> findMenuTreeAll();
    
    /**
     * 根据所有权限
     *
     * @return 权限列表
     */
    List<String> findMenuPerms();
    
    /**
     * 根据菜单ID集合查询权限
     *
     * @param idList 菜单ID集合
     * @return 权限列表
     */
    List<String> findMenuPermsInIdList(List<Long> idList);
    
    /**
     * 查询选中菜单ID列表
     *
     * @param idList 菜单ID集合
     * @return 选中菜单列表
     */
    List<Long> findCheckedIdList(List<Long> idList);
    
    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    int hasChildByMenuId(Long menuId);
    
}
