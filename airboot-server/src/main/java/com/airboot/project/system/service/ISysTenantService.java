package com.airboot.project.system.service;

import com.airboot.project.system.model.entity.SysTenant;
import com.airboot.project.system.model.vo.ExecuteSqlVO;
import com.airboot.project.system.model.vo.SearchSysTenantVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 租户Service接口
 * 
 * @author airboot
 */
public interface ISysTenantService {

    /**
     * 查询租户分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    IPage<SysTenant> getPage(SearchSysTenantVO search);

    /**
     * 查询租户列表
     * 
     * @param search 查询条件
     * @return 租户集合
     */
    List<SysTenant> getList(SearchSysTenantVO search);

    /**
     * 查询租户
     *
     * @param id 租户ID
     * @return 租户
     */
    SysTenant getById(Long id);

    /**
     * 新增租户
     * 
     * @param sysTenant 租户
     */
    void save(SysTenant sysTenant);
    
    /**
     * 修改租户
     *
     * @param sysTenant 租户
     */
    void update(SysTenant sysTenant);

    /**
     * 删除租户信息
     *
     * @param id 租户ID
     * @return 结果
     */
    int deleteById(Long id);

    /**
     * 批量删除租户
     * 
     * @param idList 需要删除的租户ID
     * @return 结果
     */
    int deleteByIds(List<Long> idList);
    
    /**
     * 执行SQL语句
     */
    int executeSql(ExecuteSqlVO vo);
    
    /**
     * 创建租户Schema
     */
    void createTenantSchema(SysTenant sysTenant) throws Exception;

}
