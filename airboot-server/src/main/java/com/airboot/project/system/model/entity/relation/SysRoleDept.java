package com.airboot.project.system.model.entity.relation;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 角色和部门关联 sys_role_dept
 *
 * @author airboot
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role_dept")
public class SysRoleDept {
    
    /**
     * 角色ID
     */
    private Long roleId;
    
    /**
     * 部门ID
     */
    private Long deptId;
    
}
