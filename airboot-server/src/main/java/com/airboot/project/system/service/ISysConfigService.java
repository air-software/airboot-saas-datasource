package com.airboot.project.system.service;

import com.airboot.project.system.model.entity.SysConfig;
import com.airboot.project.system.model.vo.SearchSysConfigVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 参数配置 服务层
 *
 * @author airboot
 */
public interface ISysConfigService {
    
    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    SysConfig getById(Long configId);
    
    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
    String getByKey(String configKey);
    
    /**
     * 根据键名查询无需登录验证的参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
    String getUnauthByKey(String configKey);
    
    /**
     * 查询分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    IPage<SysConfig> getPage(SearchSysConfigVO search);
    
    /**
     * 查询参数配置列表
     *
     * @param search 查询条件
     * @return 参数配置集合
     */
    List<SysConfig> getList(SearchSysConfigVO search);
    
    /**
     * 保存或更新参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    void saveOrUpdate(SysConfig config);
    
    /**
     * 删除参数配置信息
     *
     * @param configId 参数ID
     * @return 结果
     */
    int deleteById(Long configId);
    
    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     * @return 结果
     */
    int deleteByIds(List<Long> configIds);
    
    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数信息
     * @return 结果
     */
    boolean checkConfigKeyUnique(SysConfig config);
}
