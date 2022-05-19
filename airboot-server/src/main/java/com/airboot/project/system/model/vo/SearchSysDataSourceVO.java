package com.airboot.project.system.model.vo;

import com.airboot.common.model.vo.BaseSearchVO;
import lombok.Data;

/**
 * 查询数据源分页条件
 * 
 * @author airboot
 */
@Data
public class SearchSysDataSourceVO extends BaseSearchVO {
    
    /**
     * 数据源名称
     */
    private String name;
    
    /**
     * 主机地址
     */
    private String host;

}
