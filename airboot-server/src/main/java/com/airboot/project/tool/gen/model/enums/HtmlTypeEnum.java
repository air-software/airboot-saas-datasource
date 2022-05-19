package com.airboot.project.tool.gen.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 显示类型枚举
 *
 * @author airboot
 */
@Getter
@AllArgsConstructor
public enum HtmlTypeEnum {
    文本框("input"),
    文本域("textarea"),
    下拉框("select"),
    复选框("checkbox"),
    单选框("radio"),
    日期控件("datetime"),
    ;
    
    @EnumValue
    private String code;
}
