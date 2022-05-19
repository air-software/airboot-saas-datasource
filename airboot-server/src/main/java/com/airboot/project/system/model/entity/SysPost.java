package com.airboot.project.system.model.entity;

import com.airboot.common.core.aspectj.lang.annotation.Excel;
import com.airboot.common.model.entity.BaseEntity;
import com.airboot.common.model.enums.StatusEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 岗位表 sys_post
 *
 * @author airboot
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_post")
public class SysPost extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 岗位编码
     */
    @Excel(name = "岗位编码")
    @NotBlank(message = "岗位编码不能为空")
    @Size(min = 0, max = 64, message = "岗位编码长度不能超过64个字符")
    private String postCode;
    
    /**
     * 岗位名称
     */
    @Excel(name = "岗位名称")
    @NotBlank(message = "岗位名称不能为空")
    @Size(min = 0, max = 50, message = "岗位名称长度不能超过50个字符")
    private String postName;
    
    /**
     * 岗位排序
     */
    @Excel(name = "岗位排序")
    @NotBlank(message = "显示顺序不能为空")
    private String postSort;
    
    /**
     * 状态（0停用 1正常）
     */
    @Excel(name = "状态")
    private StatusEnum status;
    
}
