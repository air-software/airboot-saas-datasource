package com.airboot.project.monitor.service.impl;

import com.airboot.project.monitor.mapper.SysOperLogMapper;
import com.airboot.project.monitor.model.entity.SysOperLog;
import com.airboot.project.monitor.model.vo.SearchSysOperLogVO;
import com.airboot.project.monitor.service.ISysOperLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 操作日志 服务层处理
 *
 * @author airboot
 */
@Service
public class SysOperLogServiceImpl implements ISysOperLogService {
    
    @Resource
    private SysOperLogMapper operLogMapper;
    
    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    @Override
    public void save(SysOperLog operLog) {
        operLogMapper.insert(operLog);
    }
    
    /**
     * 查询系统操作日志分页
     *
     * @param search 查询条件
     * @return 操作日志分页
     */
    @Override
    public IPage<SysOperLog> getPage(SearchSysOperLogVO search) {
        return operLogMapper.findPage(search);
    }
    
    /**
     * 查询系统操作日志集合
     *
     * @param search 查询条件
     * @return 操作日志集合
     */
    @Override
    public List<SysOperLog> getList(SearchSysOperLogVO search) {
        return operLogMapper.findList(search);
    }
    
    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteByIds(List<Long> operIds) {
        return operLogMapper.deleteBatchIds(operIds);
    }
    
    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public SysOperLog getById(Long operId) {
        return operLogMapper.selectById(operId);
    }
    
    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog() {
        operLogMapper.cleanOperLog();
    }
}
