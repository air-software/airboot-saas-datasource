package com.airboot.project.system.mapper.relation;

import com.airboot.common.component.MyBaseMapper;
import com.airboot.project.system.model.entity.relation.SysUserPost;

import java.util.List;

/**
 * 用户与岗位关联表 数据层
 *
 * @author airboot
 */
public interface SysUserPostMapper extends MyBaseMapper<SysUserPost> {
    
    /**
     * 通过用户ID删除用户和岗位关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    int deleteByUserId(Long userId);
    
    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    int countByPostId(Long postId);
    
    /**
     * 批量删除用户和岗位关联
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteByUserIds(Long[] ids);
    
    /**
     * 批量新增用户岗位信息
     *
     * @param userPostList 用户角色列表
     * @return 结果
     */
    int batchInsert(List<SysUserPost> userPostList);
}
