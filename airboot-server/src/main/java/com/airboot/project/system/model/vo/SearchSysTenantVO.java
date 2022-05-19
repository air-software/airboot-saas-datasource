package com.airboot.project.system.model.vo;

import com.airboot.common.model.vo.BaseSearchVO;
import com.airboot.project.system.model.enums.TenantTypeEnum;
import lombok.Data;

/**
 * 查询租户分页条件
 * 
 * @author airboot
 */
@Data
public class SearchSysTenantVO extends BaseSearchVO {
    
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 数据库名
     */
    private String schemaName;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * 负责人姓名
     */
    private String personName;

    /**
     * 负责人手机号码
     */
    private String mobile;
    
    /**
     * 租户类型
     */
    private TenantTypeEnum tenantType;

}
