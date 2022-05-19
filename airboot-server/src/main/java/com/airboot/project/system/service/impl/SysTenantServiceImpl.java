package com.airboot.project.system.service.impl;

import com.airboot.common.core.config.properties.AppProp;
import com.airboot.common.core.constant.Constants;
import com.airboot.common.core.exception.CustomException;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.common.core.utils.sql.SqlUtil;
import com.airboot.project.system.mapper.SysDataSourceMapper;
import com.airboot.project.system.mapper.SysSqlMapper;
import com.airboot.project.system.mapper.SysTenantMapper;
import com.airboot.project.system.model.entity.SysDataSource;
import com.airboot.project.system.model.entity.SysTenant;
import com.airboot.project.system.model.vo.ExecuteSqlVO;
import com.airboot.project.system.model.vo.SearchSysTenantVO;
import com.airboot.project.system.service.ISysTenantService;
import com.airsoftware.saas.datasource.context.SaaSDataSource;
import com.airsoftware.saas.datasource.context.SaaSDataSourcePool;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 租户Service业务层处理
 * 
 * @author airboot
 */
@Slf4j
@Service
public class SysTenantServiceImpl implements ISysTenantService {

    @Resource
    private SysTenantMapper sysTenantMapper;
    
    @Resource
    private SysDataSourceMapper sysDataSourceMapper;
    
    @Resource
    private SysSqlMapper sysSqlMapper;

    /**
     * 查询租户分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    @Override
    public IPage<SysTenant> getPage(SearchSysTenantVO search) {
        return sysTenantMapper.findPage(search);
    }

    /**
     * 查询租户列表
     * 
     * @param search 查询条件
     * @return 租户
     */
    @Override
    public List<SysTenant> getList(SearchSysTenantVO search) {
        return sysTenantMapper.findList(search);
    }

    /**
     * 查询租户
     *
     * @param id 租户ID
     * @return 租户
     */
    @Override
    public SysTenant getById(Long id) {
        return sysTenantMapper.selectById(id);
    }

    /**
     * 新增租户
     * 
     * @param sysTenant 租户
     * @return 结果
     */
    @Override
    public void save(SysTenant sysTenant) {
        try {
            // 预先生成分布式唯一id
            sysTenant.setId(IdWorker.getId());
            // 生成租户数据库
            createTenantSchema(sysTenant);
            sysTenantMapper.insert(sysTenant);
        } catch (Exception e) {
            log.error("---新增租户异常, sysTenant={}, 错误信息：{}---", sysTenant, e.getClass().toString() + ": " + e.getMessage(), e);
            // 手动回滚
            if (sysTenant.getId() != null) {
                sysTenantMapper.deleteById(sysTenant.getId());
            }
            throw new CustomException("新增租户异常", e);
        }
    }
    
    /**
     * 修改租户
     *
     * @param sysTenant 租户
     */
    @Override
    public void update(SysTenant sysTenant) {
        sysTenantMapper.updateById(sysTenant);
        // 移除缓存池中的数据源，下次将从数据库中加载最新的数据源配置
        SaaSDataSourcePool.remove(sysTenant.getId());
    }

    /**
     * 删除租户信息
     *
     * @param id 租户ID
     * @return 结果
     */
    @Override
    public int deleteById(Long id) {
        return sysTenantMapper.deleteById(id);
    }

    /**
     * 批量删除租户
     * 
     * @param idList 需要删除的租户ID
     * @return 结果
     */
    @Override
    public int deleteByIds(List<Long> idList) {
        return sysTenantMapper.deleteBatchIds(idList);
    }
    
    /**
     * 执行SQL语句
     */
    @Override
    public int executeSql(ExecuteSqlVO vo) {
        // 为安全起见，此处使用hashCode而非明文匹配。默认密钥为"airboot"，正式使用时请改为你自己设定的密钥。
        if (vo.getSecret().hashCode() != -992084164) {
            throw new CustomException("密钥错误！");
        }
        log.info("---准备执行SQL, vo={}---", vo);
        int result = 0;
        if ("all".equals(vo.getDsKey())) {
            // 对全部租户执行
            List<SysTenant> tenantList = getList(null);
            for (SysTenant tenant : tenantList) {
                try {
                    log.info("---开始执行, tenant={}---", tenant);
                    SaaSDataSource.switchTo(tenant.getId());
                    int execCount = sysSqlMapper.executeSql(vo.getSqlStr());
                    SaaSDataSource.clearCurrent();
                    log.info("---执行完毕, tenant={}, execCount={}---", tenant, execCount);
                    result += execCount;
                } catch (Exception e) {
                    log.error("---全部执行时某租户库异常, tenant={}, 错误信息={}", tenant, e);
                }
            }
        } else {
            // 指定租户执行
            try {
                SaaSDataSource.switchTo(vo.getDsKey());
                result = sysSqlMapper.executeSql(vo.getSqlStr());
                SaaSDataSource.clearCurrent();
            } catch (Exception e) {
                log.error("---指定租户库执行异常, dsKey={}, 错误信息={}", vo.getDsKey(), e);
            }
        }
        return result;
    }
    
    /**
     * 创建租户Schema
     */
    @Override
    public void createTenantSchema(SysTenant sysTenant) throws Exception {
        // 如果没有填写Schema（数据库）名称，则由系统生成
        String schemaName = StringUtils.isBlank(sysTenant.getSchemaName()) ? Constants.TENANT_SCHEMA_PREFIX + sysTenant.getId() : sysTenant.getSchemaName();
        sysTenant.setSchemaName(schemaName);
        // 获取数据源配置
        SysDataSource dataSource = sysDataSourceMapper.selectById(sysTenant.getDataSourceId());
        // 先使用不指定Schema的连接
        String pureJdbcUrl = Constants.JDBC_URL_PREFIX + dataSource.getHost() + "/" + Constants.JDBC_URL_SUFFIX;
        // 创建Schema
        SqlUtil.createSchema(pureJdbcUrl, dataSource.getUsername(), dataSource.getPassword(), schemaName);
        // 指定新创建的Schema连接
        String jdbcUrl = Constants.JDBC_URL_PREFIX + dataSource.getHost() + "/" + schemaName + Constants.JDBC_URL_SUFFIX;
        // 执行指定的新建租户SQL脚本
        SqlUtil.executeScript(jdbcUrl, dataSource.getUsername(), dataSource.getPassword(), AppProp.TENANT_SQL_SCRIPT_NAME);
    }

}
