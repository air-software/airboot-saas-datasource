package com.airboot.project.monitor.service;

import com.airboot.project.monitor.model.entity.SysLogininfor;
import com.airboot.project.monitor.model.vo.SearchSysLogininforVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 系统访问日志情况信息 服务层
 *
 * @author airboot
 */
public interface ISysLogininforService {
    
    /**
     * 新增系统登录日志
     *
     * @param logininfor 访问日志对象
     */
    void save(SysLogininfor logininfor);
    
    /**
     * 查询系统登录日志分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    IPage<SysLogininfor> getPage(SearchSysLogininforVO search);
    
    /**
     * 查询系统登录日志集合
     *
     * @param search 查询条件
     * @return 登录记录集合
     */
    List<SysLogininfor> getList(SearchSysLogininforVO search);
    
    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return
     */
    int deleteByIds(List<Long> infoIds);
    
    /**
     * 清空系统登录日志
     */
    void cleanLogininfor();
}
