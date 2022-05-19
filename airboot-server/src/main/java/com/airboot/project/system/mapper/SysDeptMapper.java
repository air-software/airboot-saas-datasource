package com.airboot.project.system.mapper;

import com.airboot.common.component.MyBaseMapper;
import com.airboot.project.system.model.entity.SysDept;
import com.airboot.project.system.model.vo.SearchSysDeptVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门管理 数据层
 *
 * @author airboot
 */
public interface SysDeptMapper extends MyBaseMapper<SysDept> {
    
    /**
     * 查询部门管理数据
     *
     * @param search 查询条件
     * @return 部门信息集合
     */
    List<SysDept> findList(SearchSysDeptVO search);
    
    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    List<Long> findIdListByRoleId(Long roleId);
    
    /**
     * 根据ID查询所有子部门
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    List<SysDept> findChildrenDeptById(Long deptId);
    
    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    int findNormalChildrenDeptById(Long deptId);
    
    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    int hasChildByDeptId(Long deptId);
    
    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果
     */
    int checkDeptExistUser(Long deptId);
    
    /**
     * 校验部门名称是否唯一
     *
     * @param deptName 部门名称
     * @param parentId 父部门ID
     * @return 结果
     */
    SysDept checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") Long parentId);
    
    /**
     * 修改所在部门的父级部门状态
     *
     * @param dept 部门
     */
    void updateDeptStatus(SysDept dept);
    
    /**
     * 修改子元素关系
     *
     * @param depts 子元素
     * @return 结果
     */
    int updateDeptChildren(@Param("depts") List<SysDept> depts);
    
    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    int deleteDeptById(Long deptId);
}
