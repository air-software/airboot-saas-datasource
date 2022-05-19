package com.airboot.project.system.service;

import com.airboot.project.system.model.entity.SysRole;
import com.airboot.project.system.model.vo.SearchSysRoleVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Set;

/**
 * 角色业务层
 *
 * @author airboot
 */
public interface ISysRoleService {
    
    /**
     * 查询分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    IPage<SysRole> getPage(SearchSysRoleVO search);
    
    /**
     * 根据条件查询角色数据
     *
     * @param search 查询条件
     * @return 角色数据集合信息
     */
    List<SysRole> getList(SearchSysRoleVO search);
    
    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> getPermsByUserId(Long userId);
    
    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    List<SysRole> getAll();
    
    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    List<Long> getIdListByUserId(Long userId);
    
    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    SysRole getById(Long roleId);
    
    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    boolean checkRoleNameUnique(SysRole role);
    
    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    boolean checkRoleKeyUnique(SysRole role);
    
    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    void checkRoleAllowed(SysRole role);
    
    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    int countUserRoleByRoleId(Long roleId);
    
    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    int save(SysRole role);
    
    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    int update(SysRole role);
    
    /**
     * 修改角色状态
     *
     * @param role 角色信息
     * @return 结果
     */
    void updateStatus(SysRole role);
    
    /**
     * 修改数据权限信息
     *
     * @param role 角色信息
     * @return 结果
     */
    int authDataScope(SysRole role);
    
    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    int deleteByIds(Long[] roleIds);
}
