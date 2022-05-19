package com.airboot.project.system.model.vo;

import com.airboot.common.model.vo.BaseSearchVO;
import lombok.Data;

/**
 * 查询岗位分页条件
 *
 * @author airboot
 */
@Data
public class SearchSysPostVO extends BaseSearchVO {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 岗位编码
     */
    private String postCode;
    
    /**
     * 岗位名称
     */
    private String postName;
    
}
