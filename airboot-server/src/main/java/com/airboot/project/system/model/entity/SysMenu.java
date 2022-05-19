package com.airboot.project.system.model.entity;

import com.airboot.common.model.entity.BaseEntity;
import com.airboot.common.model.enums.StatusEnum;
import com.airboot.project.system.model.enums.MenuTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单权限表 sys_menu
 *
 * @author airboot
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_menu")
public class SysMenu extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    @Size(min = 0, max = 50, message = "菜单名称长度不能超过50个字符")
    private String menuName;
    
    /**
     * 父菜单名称
     */
    @TableField(exist = false)
    private String parentName;
    
    /**
     * 父菜单ID
     */
    private Long parentId;
    
    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    private Integer orderNum;
    
    /**
     * 路由地址
     */
    @Size(min = 0, max = 100, message = "路由地址不能超过100个字符")
    private String path;
    
    /**
     * 组件路径
     */
    @Size(min = 0, max = 150, message = "组件路径不能超过150个字符")
    private String component;
    
    /**
     * 是否为外链（false=否,true=是）
     */
    private Boolean iframe;
    
    /**
     * 类型（0目录 1菜单 2按钮）
     */
    @NotNull(message = "菜单类型不能为空")
    private MenuTypeEnum menuType;
    
    /**
     * 是否隐藏（0显示 1隐藏）
     */
    private Boolean hidden;
    
    /**
     * 菜单状态（0停用 1正常）
     */
    private StatusEnum status;
    
    /**
     * 权限字符串
     */
    @Size(min = 0, max = 100, message = "权限标识长度不能超过100个字符")
    private String perms;
    
    /**
     * 菜单图标
     */
    private String icon;
    
    /**
     * 子菜单
     */
    @Builder.Default
    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();
    
}
