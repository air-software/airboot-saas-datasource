package com.airboot.common.core.saas;

import com.airboot.common.core.constant.Constants;
import com.airboot.project.system.mapper.SysDataSourceMapper;
import com.airboot.project.system.model.entity.SysDataSource;
import com.airboot.project.system.mapper.SysTenantMapper;
import com.airboot.project.system.model.entity.SysTenant;
import com.airsoftware.saas.datasource.core.SaaSDataSourceCreator;
import com.airsoftware.saas.datasource.provider.SaaSDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * 自定义数据源提供者
 *
 * @author airboot
 */
@Component
public class MySaaSDataSourceProvider implements SaaSDataSourceProvider {

	@Resource
	private SysDataSourceMapper sysDataSourceMapper;
	
	@Resource
	private SysTenantMapper sysTenantMapper;
	
	@Resource
	private SaaSDataSourceCreator saasDataSourceCreator;
	
	/**
	 * 创建数据源
	 * @param tenantId 以租户ID为数据源的key
	 */
	@Override
	public DataSource createDataSource(String tenantId) {
		SysTenant tenant = sysTenantMapper.selectById(tenantId);
		SysDataSource dataSource = sysDataSourceMapper.selectById(tenant.getDataSourceId());
		String jdbcUrl = Constants.JDBC_URL_PREFIX + dataSource.getHost() + "/" + tenant.getSchemaName() + Constants.JDBC_URL_SUFFIX;
		DataSourceProperty dataSourceProperty=new DataSourceProperty();
		dataSourceProperty.setUrl(jdbcUrl);
		dataSourceProperty.setUsername(dataSource.getUsername());
		dataSourceProperty.setPassword(dataSource.getPassword());
		dataSourceProperty.setPoolName(tenantId);
		// 驱动类名，如果你准备使用的mysql-connector-java版本不支持SPI，则需要在数据源表中加上driver_class_name字段，并在此处进行设置。
		// dataSourceProperty.setDriverClassName(dataSource.getDriverClassName());
		
		return saasDataSourceCreator.createDruidDataSource(dataSourceProperty);
	}
	
}
