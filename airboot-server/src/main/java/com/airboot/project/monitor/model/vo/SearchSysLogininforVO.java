package com.airboot.project.monitor.model.vo;

import com.airboot.common.model.enums.LoginResultEnum;
import com.airboot.common.model.vo.BaseSearchVO;
import lombok.Data;

/**
 * 查询系统访问记录分页条件
 *
 * @author airboot
 */
@Data
public class SearchSysLogininforVO extends BaseSearchVO {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户登录账号
     */
    private String account;
    
    /**
     * 登录IP地址
     */
    private String ipaddr;
    
    /**
     * 登录结果
     */
    private LoginResultEnum loginResult;

}
