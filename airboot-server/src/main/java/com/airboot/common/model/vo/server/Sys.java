package com.airboot.common.model.vo.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 系统相关信息
 *
 * @author airboot
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Sys {
    
    /**
     * 服务器名称
     */
    private String computerName;
    
    /**
     * 服务器Ip
     */
    private String computerIp;
    
    /**
     * 项目路径
     */
    private String userDir;
    
    /**
     * 操作系统
     */
    private String osName;
    
    /**
     * 系统架构
     */
    private String osArch;
    
}
