package com.airboot.project.system.model.entity;

import com.airboot.common.model.entity.BaseEntity;
import com.airboot.common.model.enums.StatusEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门表 sys_dept
 *
 * @author airboot
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_dept")
public class SysDept extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 父部门ID
     */
    private Long parentId;
    
    /**
     * 祖级列表
     */
    private String ancestors;
    
    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空")
    @Size(min = 0, max = 30, message = "部门名称长度不能超过30个字符")
    private String deptName;
    
    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    private Integer orderNum;
    
    /**
     * 负责人
     */
    @NotBlank(message = "负责人不能为空")
    private String leader;
    
    /**
     * 负责人手机号码
     */
    @NotBlank(message = "负责人手机号码不能为空")
    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    private String mobile;
    
    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    private String email;
    
    /**
     * 部门状态（0停用 1正常）
     */
    private StatusEnum status;
    
    /**
     * 父部门名称
     */
    @TableField(exist = false)
    private String parentName;
    
    /**
     * 子部门
     */
    @Builder.Default
    @TableField(exist = false)
    private List<SysDept> children = new ArrayList<>();
    
}
