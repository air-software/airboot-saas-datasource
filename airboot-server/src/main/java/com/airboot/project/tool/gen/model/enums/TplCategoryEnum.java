package com.airboot.project.tool.gen.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 模板类型枚举
 *
 * @author airboot
 */
@Getter
@AllArgsConstructor
public enum TplCategoryEnum {
    单表("crud"),
    树表("tree"),
    ;
    
    @EnumValue
    private String code;
}
