package com.airboot.project.monitor.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * cron计划策略枚举
 *
 * @author airboot
 */
@Getter
@AllArgsConstructor
public enum MisfirePolicyEnum {
    默认(0),
    立即执行(1),
    执行一次(2),
    放弃执行(3),
    ;
    
    @EnumValue
    private Integer code;
}
