package com.airboot.project.tool.gen.service.impl;

import com.airboot.common.core.constant.Constants;
import com.airboot.common.core.constant.GenConstants;
import com.airboot.common.core.exception.CustomException;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.project.tool.gen.core.util.GenUtils;
import com.airboot.project.tool.gen.core.util.VelocityInitializer;
import com.airboot.project.tool.gen.core.util.VelocityUtils;
import com.airboot.project.tool.gen.mapper.GenTableColumnMapper;
import com.airboot.project.tool.gen.mapper.GenTableMapper;
import com.airboot.project.tool.gen.model.entity.GenTable;
import com.airboot.project.tool.gen.model.entity.GenTableColumn;
import com.airboot.project.tool.gen.model.enums.TplCategoryEnum;
import com.airboot.project.tool.gen.model.vo.SearchGenTableVO;
import com.airboot.project.tool.gen.service.IGenTableService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 业务 服务层实现
 *
 * @author airboot
 */
@Slf4j
@Service
public class GenTableServiceImpl implements IGenTableService {
    
    @Resource
    private GenTableMapper genTableMapper;
    
    @Resource
    private GenTableColumnMapper genTableColumnMapper;
    
    /**
     * 查询分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    @Override
    public IPage<GenTable> getPage(SearchGenTableVO search) {
        return genTableMapper.findPage(search);
    }
    
    /**
     * 查询业务信息
     *
     * @param id 业务ID
     * @return 业务信息
     */
    @Override
    public GenTable getById(Long id) {
        GenTable genTable = genTableMapper.findById(id);
        setTableFromOptions(genTable);
        return genTable;
    }
    
    /**
     * 查询业务列表
     *
     * @param search 查询条件
     * @return 业务集合
     */
    @Override
    public List<GenTable> getList(SearchGenTableVO search) {
        return genTableMapper.findList(search);
    }
    
    /**
     * 查询据库分页
     *
     * @param search 业务信息
     * @return 数据库表集合
     */
    @Override
    public IPage<GenTable> getDbTablePage(SearchGenTableVO search) {
        return genTableMapper.findDbTableList(search);
    }
    
    /**
     * 查询据库列表
     *
     * @param genTable 业务信息
     * @return 数据库表集合
     */
    @Override
    public List<GenTable> getDbTableList(GenTable genTable) {
        return genTableMapper.findDbTableList(genTable);
    }
    
    /**
     * 查询据库列表
     *
     * @param tableNames 表名称组
     * @return 数据库表集合
     */
    @Override
    public List<GenTable> getDbTableListByNames(String[] tableNames) {
        return genTableMapper.findDbTableListByNames(tableNames);
    }
    
    /**
     * 修改业务
     *
     * @param genTable 业务信息
     * @return 结果
     */
    @Override
    @Transactional
    public void update(GenTable genTable) {
        String options = JSON.toJSONString(genTable.getParams());
        genTable.setOptions(options);
        int row = genTableMapper.updateById(genTable);
        if (row > 0) {
            for (GenTableColumn genTableColumn : genTable.getColumns()) {
                genTableColumnMapper.updateById(genTableColumn);
            }
        }
    }
    
    /**
     * 删除业务对象
     *
     * @param tableIds 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional
    public void deleteByIds(List<Long> tableIds) {
        genTableMapper.deleteBatchIds(tableIds);
        genTableColumnMapper.deleteByTableIds(tableIds);
    }
    
    /**
     * 导入表结构
     *
     * @param tableList 导入表列表
     */
    @Override
    @Transactional
    public void importGenTable(List<GenTable> tableList) {
        for (GenTable table : tableList) {
            try {
                String tableName = table.getTableName();
                GenUtils.initTable(table);
                int row = genTableMapper.insert(table);
                if (row > 0) {
                    // 保存列信息
                    List<GenTableColumn> genTableColumns = genTableColumnMapper.findDbTableColumnsByName(tableName);
                    for (GenTableColumn column : genTableColumns) {
                        GenUtils.initColumnField(column, table);
                        genTableColumnMapper.insert(column);
                    }
                }
            } catch (Exception e) {
                log.error("表名 " + table.getTableName() + " 导入失败：", e);
            }
        }
    }
    
    /**
     * 预览代码
     *
     * @param tableId 表编号
     * @return 预览数据列表
     */
    @Override
    public Map<String, String> previewCode(Long tableId) {
        Map<String, String> dataMap = new LinkedHashMap<>();
        // 查询表信息
        GenTable table = genTableMapper.findById(tableId);
        // 查询列信息
        List<GenTableColumn> columns = table.getColumns();
        setPkColumn(table, columns);
        VelocityInitializer.initVelocity();
        
        VelocityContext context = VelocityUtils.prepareContext(table);
        
        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);
            dataMap.put(template, sw.toString());
        }
        return dataMap;
    }
    
    /**
     * 生成代码
     *
     * @param tableName 表名称
     * @return 数据
     */
    @Override
    public byte[] generateCode(String tableName) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        generatorCode(tableName, zip);
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
    
    /**
     * 批量生成代码
     *
     * @param tableNames 表数组
     * @return 数据
     */
    @Override
    public byte[] generateCode(String[] tableNames) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNames) {
            generatorCode(tableName, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
    
    /**
     * 查询表信息并生成代码
     */
    private void generatorCode(String tableName, ZipOutputStream zip) {
        // 查询表信息
        GenTable table = genTableMapper.findByName(tableName);
        // 查询列信息
        List<GenTableColumn> columns = table.getColumns();
        setPkColumn(table, columns);
        
        VelocityInitializer.initVelocity();
        
        VelocityContext context = VelocityUtils.prepareContext(table);
        
        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);
            try {
                // 添加到zip
                zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, table)));
                IOUtils.write(sw.toString(), zip, Constants.UTF8);
                IOUtils.closeQuietly(sw);
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                log.error("渲染模板失败，表名：" + table.getTableName(), e);
            }
        }
    }
    
    /**
     * 修改保存参数校验
     *
     * @param genTable 业务信息
     */
    @Override
    public void validateEdit(GenTable genTable) {
        if (TplCategoryEnum.树表.equals(genTable.getTplCategory())) {
            String options = JSON.toJSONString(genTable.getParams());
            JSONObject paramsObj = JSONObject.parseObject(options);
            if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_CODE))) {
                throw new CustomException("树编码字段不能为空");
            } else if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_PARENT_CODE))) {
                throw new CustomException("树父编码字段不能为空");
            } else if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_NAME))) {
                throw new CustomException("树名称字段不能为空");
            }
        }
    }
    
    /**
     * 设置主键列信息
     *
     * @param table 业务表信息
     * @param columns  业务字段列表
     */
    public void setPkColumn(GenTable table, List<GenTableColumn> columns) {
        for (GenTableColumn column : columns) {
            if (column.isPrimaryKey()) {
                table.setPkColumn(column);
                break;
            }
        }
        if (StringUtils.isNull(table.getPkColumn())) {
            table.setPkColumn(columns.get(0));
        }
    }
    
    /**
     * 设置代码生成其他选项值
     *
     * @param genTable 设置后的生成对象
     */
    public void setTableFromOptions(GenTable genTable) {
        JSONObject paramsObj = JSONObject.parseObject(genTable.getOptions());
        if (StringUtils.isNotNull(paramsObj)) {
            String treeCode = paramsObj.getString(GenConstants.TREE_CODE);
            String treeParentCode = paramsObj.getString(GenConstants.TREE_PARENT_CODE);
            String treeName = paramsObj.getString(GenConstants.TREE_NAME);
            String parentMenuId = paramsObj.getString(GenConstants.PARENT_MENU_ID);
            String parentMenuName = paramsObj.getString(GenConstants.PARENT_MENU_NAME);
            boolean autoResultMap = paramsObj.getBooleanValue(GenConstants.AUTO_RESULT_MAP);
            
            genTable.setTreeCode(treeCode);
            genTable.setTreeParentCode(treeParentCode);
            genTable.setTreeName(treeName);
            genTable.setParentMenuId(parentMenuId);
            genTable.setParentMenuName(parentMenuName);
            genTable.setAutoResultMap(autoResultMap);
        }
    }
}
