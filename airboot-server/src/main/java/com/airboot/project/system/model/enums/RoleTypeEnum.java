package com.airboot.project.system.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色类型枚举
 *
 * @author airboot
 */
@Getter
@AllArgsConstructor
public enum RoleTypeEnum {
    超级租户管理员(-1000),
    管理员(-500),
    自定义(0),
    ;
    
    @EnumValue
    private Integer code;
}
