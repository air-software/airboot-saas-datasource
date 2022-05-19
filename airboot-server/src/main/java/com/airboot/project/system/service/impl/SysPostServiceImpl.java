package com.airboot.project.system.service.impl;

import com.airboot.common.core.exception.CustomException;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.project.system.mapper.SysPostMapper;
import com.airboot.project.system.mapper.relation.SysUserPostMapper;
import com.airboot.project.system.model.entity.SysPost;
import com.airboot.project.system.model.vo.SearchSysPostVO;
import com.airboot.project.system.service.ISysPostService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 岗位信息 服务层处理
 *
 * @author airboot
 */
@Service
public class SysPostServiceImpl implements ISysPostService {
    
    @Resource
    private SysPostMapper postMapper;
    
    @Resource
    private SysUserPostMapper userPostMapper;
    
    /**
     * 查询分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    @Override
    public IPage<SysPost> getPage(SearchSysPostVO search) {
        return postMapper.findPage(search);
    }
    
    /**
     * 查询岗位信息集合
     *
     * @param search 查询条件
     * @return 岗位信息集合
     */
    @Override
    public List<SysPost> getList(SearchSysPostVO search) {
        return postMapper.findList(search);
    }
    
    /**
     * 查询所有岗位
     *
     * @return 岗位列表
     */
    @Override
    public List<SysPost> getAll() {
        return postMapper.selectList(null);
    }
    
    /**
     * 通过岗位ID查询岗位信息
     *
     * @param postId 岗位ID
     * @return 角色对象信息
     */
    @Override
    public SysPost getById(Long postId) {
        return postMapper.selectById(postId);
    }
    
    /**
     * 根据用户ID获取岗位选择框列表
     *
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    @Override
    public List<Long> getIdListByUserId(Long userId) {
        return postMapper.findIdListByUserId(userId);
    }
    
    /**
     * 校验岗位名称是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public boolean checkPostNameUnique(SysPost post) {
        if (StringUtils.isBlank(post.getPostName())) {
            return false;
        }
        Long postId = StringUtils.isNull(post.getId()) ? -1L : post.getId();
        SysPost info = postMapper.getOne(new LambdaQueryWrapper<SysPost>().eq(SysPost::getPostName, post.getPostName()), false);
        return info == null || info.getId().equals(postId);
    }
    
    /**
     * 校验岗位编码是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public boolean checkPostCodeUnique(SysPost post) {
        if (StringUtils.isBlank(post.getPostCode())) {
            return false;
        }
        Long postId = StringUtils.isNull(post.getId()) ? -1L : post.getId();
        SysPost info = postMapper.getOne(new LambdaQueryWrapper<SysPost>().eq(SysPost::getPostCode, post.getPostCode()), false);
        return info == null || info.getId().equals(postId);
    }
    
    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public int countUserPostById(Long postId) {
        return userPostMapper.countByPostId(postId);
    }
    
    /**
     * 删除岗位信息
     *
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public int deleteById(Long postId) {
        SysPost post = getById(postId);
        if (countUserPostById(postId) > 0) {
            throw new CustomException(String.format("%1$s已分配,不能删除", post.getPostName()));
        }
        return postMapper.deleteById(postId);
    }
    
    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     * @return 结果
     * @throws Exception 异常
     */
    @Override
    public int deleteByIds(List<Long> postIds) {
        for (Long postId : postIds) {
            SysPost post = getById(postId);
            if (countUserPostById(postId) > 0) {
                throw new CustomException(String.format("%1$s已分配,不能删除", post.getPostName()));
            }
        }
        return postMapper.deleteBatchIds(postIds);
    }
    
    /**
     * 保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public void saveOrUpdate(SysPost post) {
        postMapper.saveOrUpdate(post);
    }
    
}
