package com.airboot.project.monitor.model.vo;

import com.airboot.common.model.vo.BaseSearchVO;
import lombok.Data;

/**
 * 查询定时任务分页条件
 *
 * @author airboot
 */
@Data
public class SearchSysJobVO extends BaseSearchVO {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 任务名称
     */
    private String jobName;
    
    /**
     * 任务组名
     */
    private String jobGroup;
    
    /**
     * 调用目标字符串
     */
    private String invokeTarget;
    
}
