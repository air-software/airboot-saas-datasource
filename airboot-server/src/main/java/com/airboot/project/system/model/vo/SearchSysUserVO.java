package com.airboot.project.system.model.vo;

import com.airboot.common.model.vo.BaseSearchVO;
import lombok.Data;

/**
 * 查询用户分页条件
 *
 * @author airboot
 */
@Data
public class SearchSysUserVO extends BaseSearchVO {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 部门ID
     */
    private Long deptId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 姓名
     */
    private String personName;
    
    /**
     * 手机号码
     */
    private String mobile;

}
