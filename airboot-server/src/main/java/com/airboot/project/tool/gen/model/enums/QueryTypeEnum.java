package com.airboot.project.tool.gen.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 查询方式枚举
 *
 * @author airboot
 */
@Getter
@AllArgsConstructor
public enum QueryTypeEnum {
    等于("EQ"),
    不等于("NE"),
    大于("GT"),
    大于等于("GTE"),
    小于("LT"),
    小于等于("LTE"),
    模糊("LIKE"),
    范围("BETWEEN"),
    ;
    
    @EnumValue
    private String code;
}
