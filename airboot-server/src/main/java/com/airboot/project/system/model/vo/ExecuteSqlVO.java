package com.airboot.project.system.model.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ExecuteSqlVO {
    
    /**
     * 数据源key，如不指定则对所有租户执行
     */
    @NotBlank(message = "数据源不能为空")
    private String dsKey;
    
    /**
     * SQL语句
     */
    @NotBlank(message = "SQL语句不能为空")
    private String sqlStr;
    
    /**
     * 密钥
     */
    @NotBlank(message = "密钥不能为空")
    private String secret;
    
}
