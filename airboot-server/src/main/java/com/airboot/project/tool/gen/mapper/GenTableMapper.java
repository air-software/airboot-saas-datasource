package com.airboot.project.tool.gen.mapper;

import com.airboot.common.component.MyBaseMapper;
import com.airboot.project.tool.gen.model.entity.GenTable;
import com.airboot.project.tool.gen.model.vo.SearchGenTableVO;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 业务 数据层
 *
 * @author airboot
 */
@DS("common")
public interface GenTableMapper extends MyBaseMapper<GenTable> {
    
    /**
     * 查询分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    IPage<GenTable> findPage(SearchGenTableVO search);
    
    /**
     * 查询业务列表
     *
     * @param search 查询条件
     * @return 业务集合
     */
    List<GenTable> findList(SearchGenTableVO search);
    
    /**
     * 查询数据库分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    IPage<GenTable> findDbTableList(SearchGenTableVO search);
    
    /**
     * 查询数据库列表
     *
     * @param genTable 业务信息
     * @return 数据库表集合
     */
    List<GenTable> findDbTableList(GenTable genTable);
    
    /**
     * 查询据库列表
     *
     * @param tableNames 表名称组
     * @return 数据库表集合
     */
    List<GenTable> findDbTableListByNames(String[] tableNames);
    
    /**
     * 查询表ID业务信息
     *
     * @param id 业务ID
     * @return 业务信息
     */
    GenTable findById(Long id);
    
    /**
     * 查询表名称业务信息
     *
     * @param tableName 表名称
     * @return 业务信息
     */
    GenTable findByName(String tableName);
    
}
