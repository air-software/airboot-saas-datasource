package com.airboot.project.system.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 公告类型枚举
 *
 * @author airboot
 */
@Getter
@AllArgsConstructor
public enum NoticeTypeEnum {
    通知(1),
    公告(2),
    ;
    
    @EnumValue
    private Integer code;
}
