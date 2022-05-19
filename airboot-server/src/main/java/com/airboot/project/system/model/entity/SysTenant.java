package com.airboot.project.system.model.entity;

import com.airboot.common.core.aspectj.lang.annotation.Excel;
import com.airboot.common.model.entity.BaseEntity;
import com.airboot.common.model.enums.StatusEnum;
import com.airboot.project.system.model.enums.TenantTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 租户对象 sys_tenant
 * 
 * @author airboot
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_tenant")
public class SysTenant extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
    /**
     * Schema（数据库）名称
     */
    private String schemaName;
    
    /**
     * 租户名称
     */
    @Excel(name = "租户名称")
    private String tenantName;

    /**
     * 负责人姓名
     */
    @Excel(name = "负责人姓名")
    private String personName;

    /**
     * 负责人手机号码
     */
    @Excel(name = "负责人手机号码")
    private String mobile;

    /**
     * 负责人邮箱
     */
    private String email;

    /**
     * 租户状态（0=停用,1=正常）
     */
    @Excel(name = "租户状态")
    private StatusEnum status;
    
    /**
     * 租户类型
     */
    @Excel(name = "租户类型")
    private TenantTypeEnum tenantType;
    
}
