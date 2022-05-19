package com.airboot.project.system.service.impl;

import com.airboot.project.system.mapper.SysNoticeMapper;
import com.airboot.project.system.model.entity.SysNotice;
import com.airboot.project.system.model.vo.SearchSysNoticeVO;
import com.airboot.project.system.service.ISysNoticeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公告 服务层实现
 *
 * @author airboot
 */
@Service
public class SysNoticeServiceImpl implements ISysNoticeService {
    
    @Resource
    private SysNoticeMapper noticeMapper;
    
    /**
     * 查询分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    @Override
    public IPage<SysNotice> getPage(SearchSysNoticeVO search) {
        return noticeMapper.findPage(search);
    }
    
    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public SysNotice getById(Long noticeId) {
        return noticeMapper.selectById(noticeId);
    }
    
    /**
     * 保存公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public void saveOrUpdate(SysNotice notice) {
        noticeMapper.saveOrUpdate(notice);
    }
    
    /**
     * 删除公告对象
     *
     * @param noticeId 公告ID
     * @return 结果
     */
    @Override
    public int deleteById(Long noticeId) {
        return noticeMapper.deleteById(noticeId);
    }
    
    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    @Override
    public int deleteByIds(List<Long> noticeIds) {
        return noticeMapper.deleteBatchIds(noticeIds);
    }
}
