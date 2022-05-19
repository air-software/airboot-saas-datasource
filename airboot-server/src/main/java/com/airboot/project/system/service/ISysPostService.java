package com.airboot.project.system.service;

import com.airboot.project.system.model.entity.SysPost;
import com.airboot.project.system.model.vo.SearchSysPostVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 岗位信息 服务层
 *
 * @author airboot
 */
public interface ISysPostService {
    
    /**
     * 查询分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    IPage<SysPost> getPage(SearchSysPostVO search);
    
    /**
     * 查询岗位信息集合
     *
     * @param search 查询条件
     * @return 岗位列表
     */
    List<SysPost> getList(SearchSysPostVO search);
    
    /**
     * 查询所有岗位
     *
     * @return 岗位列表
     */
    List<SysPost> getAll();
    
    /**
     * 通过岗位ID查询岗位信息
     *
     * @param postId 岗位ID
     * @return 角色对象信息
     */
    SysPost getById(Long postId);
    
    /**
     * 根据用户ID获取岗位选择框列表
     *
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    List<Long> getIdListByUserId(Long userId);
    
    /**
     * 校验岗位名称
     *
     * @param post 岗位信息
     * @return 结果
     */
    boolean checkPostNameUnique(SysPost post);
    
    /**
     * 校验岗位编码
     *
     * @param post 岗位信息
     * @return 结果
     */
    boolean checkPostCodeUnique(SysPost post);
    
    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    int countUserPostById(Long postId);
    
    /**
     * 删除岗位信息
     *
     * @param postId 岗位ID
     * @return 结果
     */
    int deleteById(Long postId);
    
    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     * @return 结果
     * @throws Exception 异常
     */
    int deleteByIds(List<Long> postIds);
    
    /**
     * 保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    void saveOrUpdate(SysPost post);
    
}
