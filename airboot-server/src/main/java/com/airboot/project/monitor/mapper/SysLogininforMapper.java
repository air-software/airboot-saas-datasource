package com.airboot.project.monitor.mapper;

import com.airboot.common.component.MyBaseMapper;
import com.airboot.project.monitor.model.entity.SysLogininfor;
import com.airboot.project.monitor.model.vo.SearchSysLogininforVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 系统访问日志情况信息 数据层
 *
 * @author airboot
 */
public interface SysLogininforMapper extends MyBaseMapper<SysLogininfor> {
    
    /**
     * 查询系统登录日志分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    IPage<SysLogininfor> findPage(SearchSysLogininforVO search);
    
    /**
     * 查询系统登录日志集合
     *
     * @param search 查询条件
     * @return 登录记录集合
     */
    List<SysLogininfor> findList(SearchSysLogininforVO search);
    
    /**
     * 清空系统登录日志
     *
     * @return 结果
     */
    int cleanLogininfor();
}
