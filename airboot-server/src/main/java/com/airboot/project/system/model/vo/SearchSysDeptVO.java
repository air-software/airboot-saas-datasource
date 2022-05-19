package com.airboot.project.system.model.vo;

import com.airboot.common.model.vo.BaseSearchVO;
import lombok.Data;

/**
 * 查询部门分页条件
 *
 * @author airboot
 */
@Data
public class SearchSysDeptVO extends BaseSearchVO {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 父部门ID
     */
    private Long parentId;
    
    /**
     * 部门名称
     */
    private String deptName;

}
