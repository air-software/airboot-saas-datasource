package com.airboot.project.system.mapper;

import com.airboot.common.component.MyBaseMapper;
import com.airboot.project.system.model.entity.SysRole;
import com.airboot.project.system.model.vo.SearchSysRoleVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 角色表 数据层
 *
 * @author airboot
 */
public interface SysRoleMapper extends MyBaseMapper<SysRole> {
    
    /**
     * 查询分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    IPage<SysRole> findPage(SearchSysRoleVO search);
    
    /**
     * 根据条件查询角色数据
     *
     * @param search 查询条件
     * @return 角色数据集合信息
     */
    List<SysRole> findList(SearchSysRoleVO search);
    
    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> findListByUserId(Long userId);
    
    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    List<SysRole> findAll();
    
    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    List<Long> findIdListByUserId(Long userId);
    
    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    SysRole findById(Long roleId);
    
    /**
     * 校验角色名称是否唯一
     *
     * @param roleName 角色名称
     * @return 角色信息
     */
    SysRole checkRoleNameUnique(String roleName);
    
    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    int deleteRoleByIds(Long[] roleIds);
}
