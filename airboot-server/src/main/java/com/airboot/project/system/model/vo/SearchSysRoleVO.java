package com.airboot.project.system.model.vo;

import com.airboot.common.model.vo.BaseSearchVO;
import com.airboot.project.system.model.enums.RoleTypeEnum;
import lombok.Data;

/**
 * 查询角色分页条件
 *
 * @author airboot
 */
@Data
public class SearchSysRoleVO extends BaseSearchVO {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 角色名称
     */
    private String roleName;
    
    /**
     * 角色类型
     */
    private RoleTypeEnum roleType;

}
