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
 * 参数配置表 sys_config
 *
 * @author airboot
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_config")
public class SysConfig extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 参数名称
     */
    @Excel(name = "参数名称")
    @NotBlank(message = "参数名称不能为空")
    @Size(min = 0, max = 100, message = "参数名称长度不能超过100个字符")
    private String configName;
    
    /**
     * 参数键名
     */
    @Excel(name = "参数键名")
    @NotBlank(message = "参数键名不能为空")
    @Size(min = 0, max = 100, message = "参数键名长度不能超过100个字符")
    private String configKey;
    
    /**
     * 参数键值
     */
    @Excel(name = "参数键值")
    @NotBlank(message = "参数键值不能为空")
    @Size(min = 0, max = 500, message = "参数键值长度不能超过500个字符")
    private String configValue;
    
    /**
     * 是否系统内置
     */
    @Excel(name = "是否系统内置", readConverterExp = "false=否,true=是")
    private Boolean builtIn;
    
    /**
     * 是否需要登录
     */
    @Excel(name = "是否需要登录", readConverterExp = "false=否,true=是")
    private Boolean needLogin;
    
    /**
     * 状态（0停用 1正常）
     */
    @Excel(name = "状态")
    private StatusEnum status;
    
}
