package com.airboot.project.system.service.impl;

import com.airboot.common.core.utils.StringUtils;
import com.airboot.common.model.enums.StatusEnum;
import com.airboot.project.system.mapper.SysConfigMapper;
import com.airboot.project.system.model.entity.SysConfig;
import com.airboot.project.system.model.vo.SearchSysConfigVO;
import com.airboot.project.system.service.ISysConfigService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 参数配置 服务层实现
 *
 * @author airboot
 */
@Service
public class SysConfigServiceImpl implements ISysConfigService {
    
    @Resource
    private SysConfigMapper configMapper;
    
    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    public SysConfig getById(Long configId) {
        return configMapper.selectById(configId);
    }
    
    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String getByKey(String configKey) {
        SysConfig retConfig = configMapper.getOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, configKey).eq(SysConfig::getStatus, StatusEnum.正常), false);
        return StringUtils.isNotNull(retConfig) ? retConfig.getConfigValue() : "";
    }
    
    /**
     * 根据键名查询无需登录验证的参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String getUnauthByKey(String configKey) {
        SysConfig retConfig = configMapper.getOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, configKey).eq(SysConfig::getNeedLogin, false).eq(SysConfig::getStatus, StatusEnum.正常), false);
        return StringUtils.isNotNull(retConfig) ? retConfig.getConfigValue() : "";
    }
    
    /**
     * 查询分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    @Override
    public IPage<SysConfig> getPage(SearchSysConfigVO search) {
        return configMapper.findPage(search);
    }
    
    /**
     * 查询参数配置列表
     *
     * @param search 查询条件
     * @return 参数配置集合
     */
    @Override
    public List<SysConfig> getList(SearchSysConfigVO search) {
        return configMapper.findList(search);
    }
    
    /**
     * 保存或更新参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public void saveOrUpdate(SysConfig config) {
        configMapper.saveOrUpdate(config);
    }
    
    /**
     * 删除参数配置信息
     *
     * @param configId 参数ID
     * @return 结果
     */
    @Override
    public int deleteById(Long configId) {
        return configMapper.deleteById(configId);
    }
    
    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     * @return 结果
     */
    @Override
    public int deleteByIds(List<Long> configIds) {
        return configMapper.deleteBatchIds(configIds);
    }
    
    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public boolean checkConfigKeyUnique(SysConfig config) {
        if (StringUtils.isBlank(config.getConfigKey())) {
            return false;
        }
        Long configId = StringUtils.isNull(config.getId()) ? -1L : config.getId();
        SysConfig info = configMapper.getOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, config.getConfigKey()), false);
        return info == null || info.getId().equals(configId);
    }
}
