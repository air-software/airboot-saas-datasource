package com.airboot.common.model.vo.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 系统文件相关信息
 *
 * @author airboot
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SysFile {
    
    /**
     * 盘符路径
     */
    private String dirName;
    
    /**
     * 盘符类型
     */
    private String sysTypeName;
    
    /**
     * 文件类型
     */
    private String typeName;
    
    /**
     * 总大小
     */
    private String total;
    
    /**
     * 剩余大小
     */
    private String free;
    
    /**
     * 已经使用量
     */
    private String used;
    
    /**
     * 资源的使用率
     */
    private double usage;
    
}
