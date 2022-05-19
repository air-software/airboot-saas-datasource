package com.airboot.project.tool.gen.controller;

import com.airboot.common.component.BaseController;
import com.airboot.common.core.aspectj.lang.annotation.Log;
import com.airboot.common.core.config.properties.AppProp;
import com.airboot.common.core.text.Convert;
import com.airboot.common.model.vo.AjaxResult;
import com.airboot.common.security.annotation.PreAuthorize;
import com.airboot.project.monitor.model.enums.OperationTypeEnum;
import com.airboot.project.tool.gen.model.entity.GenTable;
import com.airboot.project.tool.gen.model.entity.GenTableColumn;
import com.airboot.project.tool.gen.model.vo.SearchGenTableVO;
import com.airboot.project.tool.gen.service.IGenTableColumnService;
import com.airboot.project.tool.gen.service.IGenTableService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.io.IOUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成 操作处理
 *
 * @author airboot
 */
@DS("common")
@RestController
@RequestMapping("/tool/gen")
public class GenController extends BaseController {
    
    @Resource
    private IGenTableService genTableService;
    
    @Resource
    private IGenTableColumnService genTableColumnService;
    
    /**
     * 查询代码生成列表
     */
    @PreAuthorize("tool:gen:page")
    @GetMapping("/page")
    public AjaxResult page(SearchGenTableVO search) {
        IPage<GenTable> page = genTableService.getPage(search);
        return success(page);
    }
    
    /**
     * 修改代码生成业务
     */
    @PreAuthorize("tool:gen:query")
    @GetMapping(value = "/{talbleId}")
    public AjaxResult getInfo(@PathVariable Long talbleId) {
        GenTable table = genTableService.getById(talbleId);
        List<GenTableColumn> list = genTableColumnService.getListByTableId(talbleId);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("info", table);
        dataMap.put("columns", list);
        return success(dataMap);
    }
    
    /**
     * 查询数据库列表
     */
    @PreAuthorize("tool:gen:page")
    @GetMapping("/db/page")
    public AjaxResult dataList(SearchGenTableVO search) {
        IPage<GenTable> page = genTableService.getDbTablePage(search);
        return success(page);
    }
    
    /**
     * 导入表结构（保存）
     */
    @PreAuthorize("tool:gen:import")
    @Log(title = "代码生成", operationType = OperationTypeEnum.导入)
    @PostMapping("/importTable")
    public AjaxResult importTableSave(String tables) {
        String[] tableNames = Convert.toStrArray(tables);
        // 查询表信息
        List<GenTable> tableList = genTableService.getDbTableListByNames(tableNames);
        genTableService.importGenTable(tableList);
        return success();
    }
    
    /**
     * 修改保存代码生成业务
     */
    @PreAuthorize("tool:gen:edit")
    @Log(title = "代码生成", operationType = OperationTypeEnum.修改)
    @PutMapping
    public AjaxResult editSave(@Validated @RequestBody GenTable genTable) {
        genTableService.validateEdit(genTable);
        genTableService.update(genTable);
        return success();
    }
    
    /**
     * 删除代码生成
     */
    @PreAuthorize("tool:gen:remove")
    @Log(title = "代码生成", operationType = OperationTypeEnum.删除)
    @DeleteMapping("/{tableIds}")
    public AjaxResult remove(@PathVariable Long[] tableIds) {
        genTableService.deleteByIds(Arrays.asList(tableIds));
        return success();
    }
    
    /**
     * 预览代码
     */
    @PreAuthorize("tool:gen:preview")
    @GetMapping("/preview/{tableId}")
    public AjaxResult preview(@PathVariable("tableId") Long tableId) throws IOException {
        Map<String, String> dataMap = genTableService.previewCode(tableId);
        return success(dataMap);
    }
    
    /**
     * 生成代码
     */
    @PreAuthorize("tool:gen:code")
    @Log(title = "代码生成", operationType = OperationTypeEnum.生成代码)
    @GetMapping("/genCode/{tableName}")
    public void genCode(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException {
        byte[] data = genTableService.generateCode(tableName);
        genCode(response, data);
    }
    
    /**
     * 批量生成代码
     */
    @PreAuthorize("tool:gen:code")
    @Log(title = "代码生成", operationType = OperationTypeEnum.生成代码)
    @GetMapping("/batchGenCode")
    public void batchGenCode(HttpServletResponse response, String tables) throws IOException {
        String[] tableNames = Convert.toStrArray(tables);
        byte[] data = genTableService.generateCode(tableNames);
        genCode(response, data);
    }
    
    /**
     * 生成zip文件
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        // 非生产环境下添加跨域支持
        if (AppProp.NOT_PROD_ENV) {
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"airboot.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }
}
