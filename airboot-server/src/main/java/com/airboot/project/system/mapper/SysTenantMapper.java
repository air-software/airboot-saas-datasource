package com.airboot.project.system.mapper;

import com.airboot.common.component.MyBaseMapper;
import com.airboot.project.system.model.entity.SysTenant;
import com.airboot.project.system.model.vo.SearchSysTenantVO;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 租户Mapper接口
 * 
 * @author airboot
 */
@DS("common")
public interface SysTenantMapper extends MyBaseMapper<SysTenant> {

    /**
     * 查询租户分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    IPage<SysTenant> findPage(SearchSysTenantVO search);

    /**
     * 查询租户列表
     * 
     * @param search 查询条件
     * @return 租户集合
     */
    List<SysTenant> findList(SearchSysTenantVO search);

}
