package com.airboot.project.system.mapper;

import com.airboot.common.component.MyBaseMapper;
import com.airboot.project.system.model.entity.SysPost;
import com.airboot.project.system.model.vo.SearchSysPostVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 岗位信息 数据层
 *
 * @author airboot
 */
public interface SysPostMapper extends MyBaseMapper<SysPost> {
    
    /**
     * 查询分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    IPage<SysPost> findPage(SearchSysPostVO search);
    
    /**
     * 查询岗位数据集合
     *
     * @param search 查询条件
     * @return 岗位数据集合
     */
    List<SysPost> findList(SearchSysPostVO search);
    
    /**
     * 根据用户ID获取岗位选择框列表
     *
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    List<Long> findIdListByUserId(Long userId);
    
    /**
     * 查询用户所属岗位组
     *
     * @param userId 用户ID
     * @return 结果
     */
    List<SysPost> findListByUserId(Long userId);
    
}
