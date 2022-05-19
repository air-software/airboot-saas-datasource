package com.airboot.project.system.mapper.relation;

import com.airboot.common.component.MyBaseMapper;
import com.airboot.project.system.model.entity.relation.SysRoleDept;

import java.util.List;

/**
 * 角色与部门关联表 数据层
 *
 * @author airboot
 */
public interface SysRoleDeptMapper extends MyBaseMapper<SysRoleDept> {
    
    /**
     * 通过角色ID删除角色和部门关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    int deleteByRoleId(Long roleId);
    
    /**
     * 批量删除角色部门关联信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteByRoleIds(Long[] ids);
    
    /**
     * 查询部门使用数量
     *
     * @param deptId 部门ID
     * @return 结果
     */
    int countByDeptId(Long deptId);
    
    /**
     * 批量新增角色部门信息
     *
     * @param roleDeptList 角色部门列表
     * @return 结果
     */
    int batchInsert(List<SysRoleDept> roleDeptList);
}
