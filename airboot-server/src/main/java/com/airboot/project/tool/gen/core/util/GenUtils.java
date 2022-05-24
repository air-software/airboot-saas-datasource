package com.airboot.project.tool.gen.core.util;

import com.airboot.common.core.config.GenConfig;
import com.airboot.common.core.constant.GenConstants;
import com.airboot.common.core.utils.EnumUtil;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.project.tool.gen.model.entity.GenTable;
import com.airboot.project.tool.gen.model.entity.GenTableColumn;
import com.airboot.project.tool.gen.model.enums.HtmlTypeEnum;
import com.airboot.project.tool.gen.model.enums.QueryTypeEnum;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RegExUtils;

import java.util.Arrays;

/**
 * 代码生成器 工具类
 *
 * @author airboot
 */
public class GenUtils {
    
    /**
     * 初始化表信息
     */
    public static void initTable(GenTable genTable) {
        genTable.setClassName(convertClassName(genTable.getTableName()));
        genTable.setPackageName(GenConfig.getPackageName());
        genTable.setModuleName(getModuleName(GenConfig.getPackageName()));
        genTable.setBusinessName(getBusinessName(genTable.getTableName()));
        genTable.setFunctionName(replaceText(genTable.getTableComment()));
        genTable.setFunctionAuthor(GenConfig.getAuthor());
    
        // 初始化额外选项
        JSONObject optionsJson = new JSONObject();
        // 自动生成 Mybatis 的 ResultMap
        optionsJson.put("autoResultMap", true);
        // 是否需要生成 Service 的 Interface，默认为 true
        optionsJson.put("interfaceService", true);
        genTable.setOptions(optionsJson.toJSONString());
    }
    
    /**
     * 初始化列属性字段
     */
    public static void initColumnField(GenTableColumn column, GenTable table) {
        String dataType = getDbType(column.getColumnType());
        String columnName = column.getColumnName();
        column.setTableId(table.getId());
        column.setCreatorId(table.getCreatorId());
        column.setCreatorInfo(table.getCreatorInfo());
        // 设置java字段名
        column.setJavaField(StringUtils.toCamelCase(columnName));
        
        if (arraysContains(GenConstants.COLUMNTYPE_STR, dataType)) {
            column.setJavaType(GenConstants.TYPE_STRING);
            // 字符串长度超过500设置为文本域
            Integer columnLength = getColumnLength(column.getColumnType());
            HtmlTypeEnum htmlType = columnLength >= 500 ? HtmlTypeEnum.文本域 : HtmlTypeEnum.文本框;
            column.setHtmlType(htmlType);
        } else if (arraysContains(GenConstants.COLUMNTYPE_TIME, dataType)) {
            column.setJavaType(GenConstants.TYPE_DATE);
            column.setHtmlType(HtmlTypeEnum.日期控件);
        } else if (arraysContains(GenConstants.COLUMNTYPE_NUMBER, dataType)) {
            column.setHtmlType(HtmlTypeEnum.文本框);
            
            // 如果是浮点型
            String[] strArr = StringUtils.split(StringUtils.substringBetween(column.getColumnType(), "(", ")"), ",");
            if (strArr != null && strArr.length == 2 && Integer.parseInt(strArr[1]) > 0) {
                column.setJavaType(GenConstants.TYPE_DOUBLE);
            }
            // 如果是整形
            else if (strArr != null && strArr.length == 1 && Integer.parseInt(strArr[0]) <= 12) {
                column.setJavaType(GenConstants.TYPE_INTEGER);
            }
            // 长整形
            else {
                column.setJavaType(GenConstants.TYPE_LONG);
            }
        } else if (arraysContains(GenConstants.COLUMNTYPE_ENUM, dataType)) {
            column.setJavaType(GenConstants.TYPE_ENUM);
            column.setHtmlType(HtmlTypeEnum.下拉框);
            column.setEnumFullName(EnumUtil.getEnumFullNameByTableColumn(columnName));
        } else if (arraysContains(GenConstants.COLUMNTYPE_BOOLEAN, dataType)) {
            column.setJavaType(GenConstants.TYPE_BOOLEAN);
            column.setHtmlType(HtmlTypeEnum.单选框);
        }
        
        // 插入字段（默认所有字段都需要插入）
        column.setInsertable(true);
        
        // 编辑字段
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_EDIT, columnName) && !column.isPrimaryKey()) {
            column.setEdit(true);
        }
        // 列表字段
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_LIST, columnName) && !column.isPrimaryKey()) {
            column.setList(true);
            // 列表字段都默认导入导出
            column.setExcelExport(true);
            column.setExcelImport(true);
        }
        // 查询字段
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_QUERY, columnName) && !column.isPrimaryKey()) {
            column.setQuery(true);
        }
        
        // 查询字段类型
        if (StringUtils.endsWithIgnoreCase(columnName, "name")) {
            column.setQueryType(QueryTypeEnum.模糊);
        }
        // 状态字段设置单选框
        if (StringUtils.endsWithIgnoreCase(columnName, "status")) {
            column.setHtmlType(HtmlTypeEnum.单选框);
        }
        // 类型&性别字段设置下拉框
        else if (StringUtils.endsWithIgnoreCase(columnName, "type")
                || StringUtils.endsWithIgnoreCase(columnName, "gender")) {
            column.setHtmlType(HtmlTypeEnum.下拉框);
        }
    }
    
    /**
     * 校验数组是否包含指定值
     *
     * @param arr         数组
     * @param targetValue 值
     * @return 是否包含
     */
    public static boolean arraysContains(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }
    
    /**
     * 获取模块名
     *
     * @param packageName 包名
     * @return 模块名
     */
    public static String getModuleName(String packageName) {
        int lastIndex = packageName.lastIndexOf(".");
        int nameLength = packageName.length();
        String moduleName = StringUtils.substring(packageName, lastIndex + 1, nameLength);
        return moduleName;
    }
    
    /**
     * 获取业务名
     *
     * @param tableName 表名
     * @return 业务名
     */
    public static String getBusinessName(String tableName) {
        int lastIndex = tableName.lastIndexOf("_");
        int nameLength = tableName.length();
        String businessName = StringUtils.substring(tableName, lastIndex + 1, nameLength);
        return businessName;
    }
    
    /**
     * 表名转换成Java类名
     *
     * @param tableName 表名称
     * @return 类名
     */
    public static String convertClassName(String tableName) {
        boolean autoRemovePre = GenConfig.getAutoRemovePre();
        String tablePrefix = GenConfig.getTablePrefix();
        if (autoRemovePre && StringUtils.isNotEmpty(tablePrefix)) {
            String[] searchList = StringUtils.split(tablePrefix, ",");
            tableName = replaceFirst(tableName, searchList);
        }
        return StringUtils.snakeToPascalCase(tableName);
    }
    
    /**
     * 批量替换前缀
     *
     * @param replacementm 替换值
     * @param searchList   替换列表
     * @return
     */
    public static String replaceFirst(String replacementm, String[] searchList) {
        String text = replacementm;
        for (String searchString : searchList) {
            if (replacementm.startsWith(searchString)) {
                text = replacementm.replaceFirst(searchString, "");
                break;
            }
        }
        return text;
    }
    
    /**
     * 关键字替换
     *
     * @param text 需要被替换的名字
     * @return 替换后的名字
     */
    public static String replaceText(String text) {
        return RegExUtils.replaceAll(text, "(?:表)", "");
    }
    
    /**
     * 获取数据库类型字段
     *
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    public static String getDbType(String columnType) {
        if (StringUtils.indexOf(columnType, "(") > 0) {
            return StringUtils.substringBefore(columnType, "(");
        } else {
            return columnType;
        }
    }
    
    /**
     * 获取字段长度
     *
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    public static Integer getColumnLength(String columnType) {
        if (StringUtils.indexOf(columnType, "(") > 0) {
            String length = StringUtils.substringBetween(columnType, "(", ")");
            return Integer.valueOf(length);
        } else {
            return 0;
        }
    }
}
