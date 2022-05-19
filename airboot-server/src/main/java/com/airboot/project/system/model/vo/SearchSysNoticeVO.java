package com.airboot.project.system.model.vo;

import com.airboot.common.model.vo.BaseSearchVO;
import com.airboot.project.system.model.enums.NoticeTypeEnum;
import lombok.Data;

/**
 * 查询通知公告分页条件
 *
 * @author airboot
 */
@Data
public class SearchSysNoticeVO extends BaseSearchVO {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 公告标题
     */
    private String noticeTitle;
    
    /**
     * 公告类型（1通知 2公告）
     */
    private NoticeTypeEnum noticeType;
    
    /**
     * 创建者
     */
    private String creatorInfo;

}
