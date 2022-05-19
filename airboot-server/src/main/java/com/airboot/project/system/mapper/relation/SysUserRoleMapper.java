package com.airboot.project.system.mapper.relation;

import com.airboot.common.component.MyBaseMapper;
import com.airboot.project.system.model.entity.relation.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户与角色关联表 数据层
 *
 * @author airboot
 */
public interface SysUserRoleMapper extends MyBaseMapper<SysUserRole> {
    
    /**
     * 通过用户ID删除用户和角色关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    int deleteByUserId(Long userId);
    
    /**
     * 批量删除用户和角色关联
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteByIds(Long[] ids);
    
    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    int countByRoleId(Long roleId);
    
    /**
     * 批量新增用户角色信息
     *
     * @param userRoleList 用户角色列表
     * @return 结果
     */
    int batchInsert(List<SysUserRole> userRoleList);
    
    /**
     * 删除用户和角色关联信息
     *
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    int deleteByUserIdAndRoleId(SysUserRole userRole);
    
    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    int deleteByUserIdsAndRoleId(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);
}
