package com.airboot.project.system.model.entity;

import com.airboot.common.model.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 数据源 sys_data_source
 *
 * @author airboot
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_data_source")
public class SysDataSource extends BaseEntity {
	
	/**
	 * 数据源名称
	 */
	private String name;
	
	/**
	 * 主机地址（域名或IP，支持端口号）
	 */
	private String host;
	
	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 驱动类名，如果你准备使用的mysql-connector-java版本不支持SPI，则需要在这张表中加上driver_class_name字段。
	 */
	// private String driverClassName;
}
