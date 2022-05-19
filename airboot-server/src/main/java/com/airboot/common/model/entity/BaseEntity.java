package com.airboot.common.model.entity;

import com.airboot.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity基类
 *
 * @author airboot
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * ID
     */
    @TableId
    private Long id;
    
    /**
     * 创建者id
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;
    
    /**
     * 创建时姓名_登录账号
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorInfo;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT, locale = "zh", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    /**
     * 更新者id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updaterId;
    
    /**
     * 更新时姓名_登录账号
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updaterInfo;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT, locale = "zh", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    
    /**
     * 扩展JSON
     */
    private String extJson;
    
    /**
     * 是否已删除
     */
    private Boolean deleted;
    
    /**
     * 数据版本，更新时加1
     * 需要使用数据版本时，解除下方 @Version 的注释，去掉初始值 0
     * 并解除 MybatisPlusConfig.java中的乐观锁插件注释
     * 每次更新时都要给version设置好版本号（即查询时表中的version值）
     * 支持saveOrUpdate、updateById和update方法
     * 教程见 https://baomidou.com/guide/optimistic-locker-plugin.html
     */
//    @Version
    @Builder.Default
    private Integer version = 0;
}
