package com.airboot.project.tool.gen.service;

import com.airboot.project.tool.gen.model.entity.GenTable;
import com.airboot.project.tool.gen.model.vo.SearchGenTableVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * 业务 服务层
 *
 * @author airboot
 */
public interface IGenTableService {
    
    /**
     * 查询分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    IPage<GenTable> getPage(SearchGenTableVO search);
    
    /**
     * 查询业务列表
     *
     * @param search 查询条件
     * @return 业务集合
     */
    List<GenTable> getList(SearchGenTableVO search);
    
    /**
     * 查询据库分页
     *
     * @param search 业务信息
     * @return 数据库表集合
     */
    IPage<GenTable> getDbTablePage(SearchGenTableVO search);
    
    /**
     * 查询据库列表
     *
     * @param genTable 业务信息
     * @return 数据库表集合
     */
    List<GenTable> getDbTableList(GenTable genTable);
    
    /**
     * 查询据库列表
     *
     * @param tableNames 表名称组
     * @return 数据库表集合
     */
    List<GenTable> getDbTableListByNames(String[] tableNames);
    
    /**
     * 查询业务信息
     *
     * @param id 业务ID
     * @return 业务信息
     */
    GenTable getById(Long id);
    
    /**
     * 修改业务
     *
     * @param genTable 业务信息
     * @return 结果
     */
    void update(GenTable genTable);
    
    /**
     * 删除业务信息
     *
     * @param tableIds 需要删除的表数据ID
     * @return 结果
     */
    void deleteByIds(List<Long> tableIds);
    
    /**
     * 导入表结构
     *
     * @param tableList 导入表列表
     */
    void importGenTable(List<GenTable> tableList);
    
    /**
     * 预览代码
     *
     * @param tableId 表编号
     * @return 预览数据列表
     */
    Map<String, String> previewCode(Long tableId);
    
    /**
     * 生成代码
     *
     * @param tableName 表名称
     * @return 数据
     */
    byte[] generateCode(String tableName);
    
    /**
     * 批量生成代码
     *
     * @param tableNames 表数组
     * @return 数据
     */
    byte[] generateCode(String[] tableNames);
    
    /**
     * 修改保存参数校验
     *
     * @param genTable 业务信息
     */
    void validateEdit(GenTable genTable);
}
