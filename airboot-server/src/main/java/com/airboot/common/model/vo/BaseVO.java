package com.airboot.common.model.vo;

import com.airboot.common.core.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * VO对象基本类
 *
 * @author airboot
 */
@Data
public class BaseVO {
    
    private Long id;
    
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT, locale = "zh", timezone = "GMT+8")
    private Date createTime;
    
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT, locale = "zh", timezone = "GMT+8")
    private Date updateTime;
}
