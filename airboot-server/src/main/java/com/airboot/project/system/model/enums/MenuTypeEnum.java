package com.airboot.project.system.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 菜单类型枚举
 *
 * @author airboot
 */
@Getter
@AllArgsConstructor
public enum MenuTypeEnum {
    目录(0),
    菜单(1),
    按钮(2),
    ;
    
    @EnumValue
    private Integer code;
}
