package com.airboot.common.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 查询条件通用状态枚举
 *
 * @author airboot
 */
@Getter
@AllArgsConstructor
public enum SearchStatusEnum {
    正常(1),
    成功(1),
    暂停(0),
    停用(0),
    失败(0),
    ;
    
    @EnumValue
    private Integer code;
}
