package com.airboot.project.system.service;

import com.airboot.common.model.entity.TreeSelect;
import com.airboot.project.system.model.entity.SysDept;
import com.airboot.project.system.model.vo.SearchSysDeptVO;

import java.util.List;

/**
 * 部门管理 服务层
 *
 * @author airboot
 */
public interface ISysDeptService {
    
    /**
     * 查询部门管理数据
     *
     * @param search 查询条件
     * @return 部门信息集合
     */
    List<SysDept> getList(SearchSysDeptVO search);
    
    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    List<SysDept> buildTree(List<SysDept> depts);
    
    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    List<TreeSelect> buildTreeSelect(List<SysDept> depts);
    
    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    List<Long> getListByRoleId(Long roleId);
    
    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    SysDept getById(Long deptId);
    
    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    int getNormalChildrenDeptById(Long deptId);
    
    /**
     * 是否存在部门子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    boolean hasChildByDeptId(Long deptId);
    
    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    boolean checkDeptExistUser(Long deptId);
    
    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    boolean checkDeptNameUnique(SysDept dept);
    
    /**
     * 保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    int save(SysDept dept);
    
    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    int update(SysDept dept);
    
    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    int deleteById(Long deptId);
}
