package com.airboot.project.monitor.model.vo;

import com.airboot.common.model.vo.BaseSearchVO;
import com.airboot.project.monitor.model.enums.OperationTypeEnum;
import lombok.Data;

/**
 * 查询系统访问记录分页条件
 *
 * @author airboot
 */
@Data
public class SearchSysOperLogVO extends BaseSearchVO {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 操作模块
     */
    private String title;
    
    /**
     * 操作类型
     */
    private OperationTypeEnum operationType;
    
    /**
     * 操作人员账号
     */
    private String operName;

}
