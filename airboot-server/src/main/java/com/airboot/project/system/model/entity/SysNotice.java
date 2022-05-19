package com.airboot.project.system.model.entity;

import com.airboot.common.model.entity.BaseEntity;
import com.airboot.common.model.enums.StatusEnum;
import com.airboot.project.system.model.enums.NoticeTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 通知公告表 sys_notice
 *
 * @author airboot
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_notice")
public class SysNotice extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 公告标题
     */
    @NotBlank(message = "公告标题不能为空")
    @Size(min = 0, max = 50, message = "公告标题不能超过50个字符")
    private String noticeTitle;
    
    /**
     * 公告类型（1通知 2公告）
     */
    private NoticeTypeEnum noticeType;
    
    /**
     * 公告内容
     */
    private String noticeContent;
    
    /**
     * 公告状态（0关闭 1正常）
     */
    private StatusEnum status;
    
}
