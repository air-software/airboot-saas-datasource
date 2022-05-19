package com.airboot.project.system.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据权限枚举
 *
 * @author airboot
 */
@Getter
@AllArgsConstructor
public enum DataScopeEnum {
    全部数据权限(1),
    自定义数据权限(2),
    本部门数据权限(3),
    本部门及以下数据权限(4),
    仅本人数据权限(5),
    ;
    
    @EnumValue
    private Integer code;
}
