package com.airboot.project.system.model.entity;

import com.airboot.common.core.aspectj.lang.annotation.Excel;
import com.airboot.common.core.constant.Constants;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.common.model.entity.BaseEntity;
import com.airboot.common.model.enums.StatusEnum;
import com.airboot.project.system.model.enums.DataScopeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 角色表 sys_role
 *
 * @author airboot
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role")
public class SysRole extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 角色名称
     */
    @Excel(name = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
    private String roleName;
    
    /**
     * 角色权限
     */
    @Excel(name = "角色权限")
    @NotBlank(message = "权限字符不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
    private String roleKey;
    
    /**
     * 角色排序
     */
    @Excel(name = "角色排序")
    @NotNull(message = "显示顺序不能为空")
    private Integer roleSort;
    
    /**
     * 数据范围（1：全部数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限）
     */
    @Excel(name = "数据范围")
    private DataScopeEnum dataScope;
    
    /**
     * 角色状态（0停用 1正常）
     */
    @Excel(name = "角色状态")
    private StatusEnum status;
    
    /**
     * 菜单组
     */
    @TableField(exist = false)
    private Long[] menuIds;
    
    /**
     * 部门组（数据权限）
     */
    @TableField(exist = false)
    private Long[] deptIds;
    
    /**
     * 是否为管理员角色
     */
    public boolean isAdmin() {
        return isAdmin(this.getRoleKey());
    }
    
    public static boolean isAdmin(String roleKey) {
        return StringUtils.isNotBlank(roleKey) && Constants.ADMIN_ROLE_KEY.equals(roleKey);
    }
    
}
