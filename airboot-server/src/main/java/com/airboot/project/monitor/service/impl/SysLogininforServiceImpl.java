package com.airboot.project.monitor.service.impl;

import com.airboot.project.monitor.mapper.SysLogininforMapper;
import com.airboot.project.monitor.model.entity.SysLogininfor;
import com.airboot.project.monitor.model.vo.SearchSysLogininforVO;
import com.airboot.project.monitor.service.ISysLogininforService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统访问日志情况信息 服务层处理
 *
 * @author airboot
 */
@Service
public class SysLogininforServiceImpl implements ISysLogininforService {
    
    @Resource
    private SysLogininforMapper logininforMapper;
    
    /**
     * 新增系统登录日志
     *
     * @param logininfor 访问日志对象
     */
    @Override
    public void save(SysLogininfor logininfor) {
        logininforMapper.insert(logininfor);
    }
    
    /**
     * 查询系统登录日志分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    @Override
    public IPage<SysLogininfor> getPage(SearchSysLogininforVO search) {
        return logininforMapper.findPage(search);
    }
    
    /**
     * 查询系统登录日志集合
     *
     * @param search 查询条件
     * @return 登录记录集合
     */
    @Override
    public List<SysLogininfor> getList(SearchSysLogininforVO search) {
        return logininforMapper.findList(search);
    }
    
    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return
     */
    @Override
    public int deleteByIds(List<Long> infoIds) {
        return logininforMapper.deleteBatchIds(infoIds);
    }
    
    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLogininfor() {
        logininforMapper.cleanLogininfor();
    }
}
