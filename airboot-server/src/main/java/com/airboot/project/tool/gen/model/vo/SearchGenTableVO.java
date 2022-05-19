package com.airboot.project.tool.gen.model.vo;

import com.airboot.common.model.vo.BaseSearchVO;
import lombok.Data;

/**
 * 查询代码生成表分页条件
 *
 * @author airboot
 */
@Data
public class SearchGenTableVO extends BaseSearchVO {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 表名称
     */
    private String tableName;
    
    /**
     * 表描述
     */
    private String tableComment;

}
