package com.airboot.project.monitor.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作类型枚举
 *
 * @author airboot
 */
@Getter
@AllArgsConstructor
public enum OperationTypeEnum {
    新增(1),
    修改(2),
    删除(3),
    授权(4),
    导出(5),
    导入(6),
    强退(7),
    生成代码(8),
    清空数据(9),
    其它(0),
    ;
    
    @EnumValue
    private Integer code;
}
