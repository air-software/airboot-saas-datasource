package com.airboot.project.system.model.vo;

import com.airboot.common.model.vo.BaseSearchVO;
import lombok.Data;

/**
 * 查询参数配置分页条件
 *
 * @author airboot
 */
@Data
public class SearchSysConfigVO extends BaseSearchVO {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 参数名称
     */
    private String configName;
    
    /**
     * 参数键名
     */
    private String configKey;
    
    /**
     * 是否系统内置
     */
    private Boolean builtIn;
    
    /**
     * 是否需要登录
     */
    private Boolean needLogin;

}
