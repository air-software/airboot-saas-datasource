package com.airboot.project.system.service;

import com.airboot.project.system.model.entity.SysNotice;
import com.airboot.project.system.model.vo.SearchSysNoticeVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 公告 服务层
 *
 * @author airboot
 */
public interface ISysNoticeService {
    
    /**
     * 查询分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    IPage<SysNotice> getPage(SearchSysNoticeVO search);
    
    /**
     * 查询公告信息
     *
     * @param search 查询条件
     * @return 公告信息
     */
    SysNotice getById(Long noticeId);
    
    /**
     * 保存公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    void saveOrUpdate(SysNotice notice);
    
    /**
     * 删除公告信息
     *
     * @param noticeId 公告ID
     * @return 结果
     */
    int deleteById(Long noticeId);
    
    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    int deleteByIds(List<Long> noticeIds);
}
