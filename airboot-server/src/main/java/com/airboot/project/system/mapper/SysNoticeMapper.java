package com.airboot.project.system.mapper;

import com.airboot.common.component.MyBaseMapper;
import com.airboot.project.system.model.entity.SysNotice;
import com.airboot.project.system.model.vo.SearchSysNoticeVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 通知公告表 数据层
 *
 * @author airboot
 */
public interface SysNoticeMapper extends MyBaseMapper<SysNotice> {
    
    /**
     * 查询公告分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    IPage<SysNotice> findPage(SearchSysNoticeVO search);
    
}
