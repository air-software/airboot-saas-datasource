package com.airboot.project.monitor.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务状态枚举
 *
 * @author airboot
 */
@Getter
@AllArgsConstructor
public enum JobStatusEnum {
    正常(1),
    暂停(0),
    ;
    
    @EnumValue
    private Integer code;
}
