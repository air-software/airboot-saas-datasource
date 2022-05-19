package com.airboot.project.system.service.impl;

import com.airboot.common.core.exception.CustomException;
import com.airboot.project.system.mapper.SysDataSourceMapper;
import com.airboot.project.system.mapper.SysTenantMapper;
import com.airboot.project.system.model.entity.SysDataSource;
import com.airboot.project.system.model.entity.SysTenant;
import com.airboot.project.system.model.vo.SearchSysDataSourceVO;
import com.airboot.project.system.model.vo.SearchSysTenantVO;
import com.airboot.project.system.service.ISysDataSourceService;
import com.airsoftware.saas.datasource.context.SaaSDataSourcePool;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据源Service业务层处理
 * 
 * @author airboot
 */
@Slf4j
@Service
public class SysDataSourceServiceImpl implements ISysDataSourceService {

    @Resource
    private SysDataSourceMapper sysDataSourceMapper;
    
    @Resource
    private SysTenantMapper sysTenantMapper;

    /**
     * 查询数据源分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    @Override
    public IPage<SysDataSource> getPage(SearchSysDataSourceVO search) {
        return sysDataSourceMapper.findPage(search);
    }

    /**
     * 查询数据源列表
     * 
     * @param search 查询条件
     * @return 数据源
     */
    @Override
    public List<SysDataSource> getList(SearchSysDataSourceVO search) {
        return sysDataSourceMapper.findList(search);
    }

    /**
     * 查询数据源
     *
     * @param id 数据源ID
     * @return 数据源
     */
    @Override
    public SysDataSource getById(Long id) {
        return sysDataSourceMapper.selectById(id);
    }
    
    /**
     * 修改数据源
     *
     * @param sysDataSource 数据源
     */
    @Override
    public void saveOrUpdate(SysDataSource sysDataSource) {
        // 防止用户名或密码中的特殊字符被转义
        sysDataSource.setUsername(StringEscapeUtils.unescapeHtml(sysDataSource.getUsername()));
        sysDataSource.setPassword(StringEscapeUtils.unescapeHtml(sysDataSource.getPassword()));
        if (sysDataSource.getId() != null) {
            // 如果是更新，则移除数据源缓存池中与此相关的所有租户数据源，下次将从数据库中加载最新的数据源配置
            SearchSysTenantVO search = new SearchSysTenantVO();
            search.setDataSourceId(sysDataSource.getId());
            List<SysTenant> tenantList = sysTenantMapper.findList(search);
            for (SysTenant tenant : tenantList) {
                SaaSDataSourcePool.remove(tenant.getId());
            }
        }
        sysDataSourceMapper.saveOrUpdate(sysDataSource);
    }

    /**
     * 删除数据源信息
     *
     * @param id 数据源ID
     * @return 结果
     */
    @Override
    public int deleteById(Long id) {
        checkDataSourceUsing(id);
        return sysDataSourceMapper.deleteById(id);
    }

    /**
     * 批量删除数据源
     * 
     * @param idList 需要删除的数据源ID
     * @return 结果
     */
    @Override
    public int deleteByIds(List<Long> idList) {
        for (Long id : idList) {
            checkDataSourceUsing(id);
        }
        return sysDataSourceMapper.deleteBatchIds(idList);
    }
    
    /**
     * 检查是否有租户在使用此数据源
     */
    private void checkDataSourceUsing(Long dataSourceId) {
        SearchSysTenantVO search = new SearchSysTenantVO();
        search.setDataSourceId(dataSourceId);
        if (CollectionUtils.isNotEmpty(sysTenantMapper.findList(search))) {
            throw new CustomException("存在使用所选数据源的租户，不可删除");
        }
    }

}
