package com.airboot.project.tool.gen.core.util;

import com.airboot.common.core.constant.GenConstants;
import com.airboot.common.core.exception.CustomException;
import com.airboot.common.core.utils.DateUtils;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.project.tool.gen.model.entity.GenTable;
import com.airboot.project.tool.gen.model.entity.GenTableColumn;
import com.airboot.project.tool.gen.model.enums.TplCategoryEnum;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;

import java.util.*;

@Slf4j
public class VelocityUtils {
    
    /**
     * 项目空间路径
     */
    private static final String PROJECT_PATH = "main/java";
    
    /**
     * mybatis空间路径
     */
    private static final String MYBATIS_PATH = "main/resources/mybatis";
    
    /**
     * 默认上级菜单为【系统工具】
     */
    private static final String DEFAULT_PARENT_MENU_ID = "3";
    
    /**
     * 设置模板变量信息
     *
     * @return 模板列表
     */
    public static VelocityContext prepareContext(GenTable genTable) {
        String moduleName = genTable.getModuleName();
        String businessName = genTable.getBusinessName();
        String packageName = genTable.getPackageName();
        TplCategoryEnum tplCategory = genTable.getTplCategory();
        String functionName = genTable.getFunctionName();
        
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("tplCategory", genTable.getTplCategory());
        velocityContext.put("tableName", genTable.getTableName());
        velocityContext.put("functionName", StringUtils.isNotEmpty(functionName) ? functionName : "【请填写功能名称】");
        velocityContext.put("ClassName", genTable.getClassName());
        velocityContext.put("className", StringUtils.uncapitalize(genTable.getClassName()));
        velocityContext.put("moduleName", genTable.getModuleName());
        velocityContext.put("BusinessName", StringUtils.capitalize(genTable.getBusinessName()));
        velocityContext.put("businessName", genTable.getBusinessName());
        velocityContext.put("basePackage", getPackagePrefix(packageName));
        velocityContext.put("packageName", packageName);
        velocityContext.put("author", genTable.getFunctionAuthor());
        velocityContext.put("datetime", DateUtils.getDate());
        velocityContext.put("pkColumn", genTable.getPkColumn());
        velocityContext.put("importList", getImportList(genTable.getColumns()));
        velocityContext.put("voImportList", getVoImportList(genTable.getColumns()));
        velocityContext.put("permissionPrefix", getPermissionPrefix(moduleName, businessName));
        
        prepareColumns(genTable.getColumns());
        velocityContext.put("columns", genTable.getColumns());
        velocityContext.put("table", genTable);
        
        setOtherVelocityContext(velocityContext, genTable);
        if (TplCategoryEnum.树表.equals(tplCategory)) {
            setTreeVelocityContext(velocityContext, genTable);
        }
        return velocityContext;
    }
    
    public static void setOtherVelocityContext(VelocityContext context, GenTable genTable) {
        String options = genTable.getOptions();
        JSONObject paramsObj = JSONObject.parseObject(options);
        String parentMenuId = getParentMenuId(paramsObj);
        context.put("parentMenuId", parentMenuId);
        context.put("autoResultMap", paramsObj != null && paramsObj.getBooleanValue(GenConstants.AUTO_RESULT_MAP));
        context.put("interfaceService", paramsObj != null && paramsObj.getBooleanValue(GenConstants.INTERFACE_SERVICE));
    }
    
    public static void setTreeVelocityContext(VelocityContext context, GenTable genTable) {
        String options = genTable.getOptions();
        JSONObject paramsObj = JSONObject.parseObject(options);
        String treeCode = getTreecode(paramsObj);
        String treeParentCode = getTreeParentCode(paramsObj);
        String treeName = getTreeName(paramsObj);
        
        context.put("treeCode", treeCode);
        context.put("treeParentCode", treeParentCode);
        context.put("treeName", treeName);
        context.put("expandColumn", getExpandColumn(genTable));
        if (paramsObj.containsKey(GenConstants.TREE_PARENT_CODE)) {
            context.put("tree_parent_code", paramsObj.getString(GenConstants.TREE_PARENT_CODE));
        }
        if (paramsObj.containsKey(GenConstants.TREE_NAME)) {
            context.put("tree_name", paramsObj.getString(GenConstants.TREE_NAME));
        }
    }
    
    /**
     * 获取模板信息
     *
     * @return 模板列表
     */
    public static List<String> getTemplateList(GenTable genTable) {
        List<String> templates = new ArrayList<>();
        templates.add("vm/java/model-entity.java.vm");
        templates.add("vm/java/model-vo.java.vm");
        templates.add("vm/java/mapper.java.vm");
        if (genTable.getInterfaceService()) {
            templates.add("vm/java/service.java.vm");
            templates.add("vm/java/serviceImpl.java.vm");
        } else {
            templates.add("vm/java/service-pure.java.vm");
        }
        templates.add("vm/java/controller.java.vm");
        templates.add("vm/xml/mapper.xml.vm");
        templates.add("vm/sql/sql.vm");
        templates.add("vm/js/api.js.vm");
        if (TplCategoryEnum.单表.equals(genTable.getTplCategory())) {
            templates.add("vm/vue/index.vue.vm");
        } else if (TplCategoryEnum.树表.equals(genTable.getTplCategory())) {
            templates.add("vm/vue/index-tree.vue.vm");
        }
        return templates;
    }
    
    /**
     * 获取文件名
     */
    public static String getFileName(String template, GenTable genTable) {
        // 文件名称
        String fileName = "";
        // 包路径
        String packageName = genTable.getPackageName();
        // 模块名
        String moduleName = genTable.getModuleName();
        // 大写类名
        String className = genTable.getClassName();
        // 业务名称
        String businessName = genTable.getBusinessName();
        
        String javaPath = PROJECT_PATH + "/" + StringUtils.replace(packageName, ".", "/");
        String mybatisPath = MYBATIS_PATH + "/" + moduleName;
        String vuePath = "vue";
        
        if (template.contains("model-entity.java.vm")) {
            fileName = StringUtils.format("{}/model/entity/{}.java", javaPath, className);
        } else if (template.contains("model-vo.java.vm")) {
            fileName = StringUtils.format("{}/model/vo/Search{}VO.java", javaPath, className);
        } else if (template.contains("mapper.java.vm")) {
            fileName = StringUtils.format("{}/mapper/{}Mapper.java", javaPath, className);
        } else if (template.contains("service.java.vm")) {
            fileName = StringUtils.format("{}/service/I{}Service.java", javaPath, className);
        } else if (template.contains("serviceImpl.java.vm")) {
            fileName = StringUtils.format("{}/service/impl/{}ServiceImpl.java", javaPath, className);
        } else if (template.contains("service-pure.java.vm")) {
            fileName = StringUtils.format("{}/service/{}Service.java", javaPath, className);
        } else if (template.contains("controller.java.vm")) {
            fileName = StringUtils.format("{}/controller/{}Controller.java", javaPath, className);
        } else if (template.contains("mapper.xml.vm")) {
            fileName = StringUtils.format("{}/{}Mapper.xml", mybatisPath, className);
        } else if (template.contains("sql.vm")) {
            fileName = businessName + "Menu.sql";
        } else if (template.contains("api.js.vm")) {
            fileName = StringUtils.format("{}/api/{}/{}.js", vuePath, moduleName, businessName);
        } else if (template.contains("index.vue.vm")) {
            fileName = StringUtils.format("{}/views/{}/{}/index.vue", vuePath, moduleName, businessName);
        } else if (template.contains("index-tree.vue.vm")) {
            fileName = StringUtils.format("{}/views/{}/{}/index.vue", vuePath, moduleName, businessName);
        }
        return fileName;
    }
    
    /**
     * 获取包前缀
     *
     * @param packageName 包名称
     * @return 包前缀名称
     */
    public static String getPackagePrefix(String packageName) {
        int lastIndex = packageName.lastIndexOf(".");
        String basePackage = StringUtils.substring(packageName, 0, lastIndex);
        return basePackage;
    }
    
    /**
     * 根据列类型获取导入包
     *
     * @param columns 列集合
     * @return 返回需要导入的包列表
     */
    public static HashSet<String> getImportList(List<GenTableColumn> columns) {
        HashSet<String> importList = new HashSet<>();
        for (GenTableColumn column : columns) {
            if (column.isSuperColumn()) {
                continue;
            }
            if (GenConstants.TYPE_DATE.equals(column.getJavaType())) {
                importList.add("java.util.Date");
                importList.add("com.fasterxml.jackson.annotation.JsonFormat");
                importList.add("com.airboot.common.core.utils.DateUtils");
            } else if (GenConstants.TYPE_BIGDECIMAL.equals(column.getJavaType())) {
                importList.add("java.math.BigDecimal");
            } else if (GenConstants.TYPE_ENUM.equals(column.getJavaType())) {
                importList.add(column.getEnumFullName());
            }
            // 非空校验的导入
            if (column.isRequired()) {
                if (GenConstants.TYPE_STRING.equals(column.getJavaType())) {
                    importList.add("javax.validation.constraints.NotBlank");
                } else {
                    importList.add("javax.validation.constraints.NotNull");
                }
            }
            // Excel导入导出
            if (column.isExcelExport() || column.isExcelImport()) {
                importList.add("com.airboot.common.core.aspectj.lang.annotation.Excel");
                if (!column.isExcelExport() || !column.isExcelImport()) {
                    importList.add("com.airboot.common.core.aspectj.lang.annotation.Excel.Type");
                }
            }
        }
        return importList;
    }
    
    /**
     * 根据列类型获取VO导入包
     *
     * @param columns 列集合
     * @return 返回需要导入的包列表
     */
    public static HashSet<String> getVoImportList(List<GenTableColumn> columns) {
        HashSet<String> importList = new HashSet<>();
        for (GenTableColumn column : columns) {
            if (column.isSearchSuperColumn() || !column.isQuery()) {
                continue;
            }
            if (GenConstants.TYPE_DATE.equals(column.getJavaType())) {
                importList.add("java.util.Date");
                importList.add("com.fasterxml.jackson.annotation.JsonFormat");
                importList.add("com.airboot.common.core.utils.DateUtils");
            } else if (GenConstants.TYPE_BIGDECIMAL.equals(column.getJavaType())) {
                importList.add("java.math.BigDecimal");
            } else if (GenConstants.TYPE_ENUM.equals(column.getJavaType())) {
                importList.add(column.getEnumFullName());
            }
        }
        return importList;
    }
    
    /**
     * 获取权限前缀
     *
     * @param moduleName   模块名称
     * @param businessName 业务名称
     * @return 返回权限前缀
     */
    public static String getPermissionPrefix(String moduleName, String businessName) {
        return StringUtils.format("{}:{}", moduleName, businessName);
        
    }
    
    /**
     * 获取上级菜单ID字段
     *
     * @param paramsObj 生成其他选项
     * @return 上级菜单ID字段
     */
    public static String getParentMenuId(JSONObject paramsObj) {
        if (StringUtils.isNotEmpty(paramsObj) && paramsObj.containsKey(GenConstants.PARENT_MENU_ID)) {
            return paramsObj.getString(GenConstants.PARENT_MENU_ID);
        }
        return DEFAULT_PARENT_MENU_ID;
    }
    
    /**
     * 获取树编码
     *
     * @param paramsObj 生成其他选项
     * @return 树编码
     */
    public static String getTreecode(JSONObject paramsObj) {
        if (paramsObj.containsKey(GenConstants.TREE_CODE)) {
            return StringUtils.toCamelCase(paramsObj.getString(GenConstants.TREE_CODE));
        }
        return "";
    }
    
    /**
     * 获取树父编码
     *
     * @param paramsObj 生成其他选项
     * @return 树父编码
     */
    public static String getTreeParentCode(JSONObject paramsObj) {
        if (paramsObj.containsKey(GenConstants.TREE_PARENT_CODE)) {
            return StringUtils.toCamelCase(paramsObj.getString(GenConstants.TREE_PARENT_CODE));
        }
        return "";
    }
    
    /**
     * 获取树名称
     *
     * @param paramsObj 生成其他选项
     * @return 树名称
     */
    public static String getTreeName(JSONObject paramsObj) {
        if (paramsObj.containsKey(GenConstants.TREE_NAME)) {
            return StringUtils.toCamelCase(paramsObj.getString(GenConstants.TREE_NAME));
        }
        return "";
    }
    
    /**
     * 获取需要在哪一列上面显示展开按钮
     *
     * @param genTable 业务表对象
     * @return 展开按钮列序号
     */
    public static int getExpandColumn(GenTable genTable) {
        String options = genTable.getOptions();
        JSONObject paramsObj = JSONObject.parseObject(options);
        String treeName = paramsObj.getString(GenConstants.TREE_NAME);
        int num = 0;
        for (GenTableColumn column : genTable.getColumns()) {
            if (column.isList()) {
                num++;
                String columnName = column.getColumnName();
                if (columnName.equals(treeName)) {
                    break;
                }
            }
        }
        return num;
    }
    
    /**
     * 针对columns做一些前置处理
     */
    public static void prepareColumns(List<GenTableColumn> columns) {
        for (GenTableColumn column : columns) {
            // 为枚举类型补全信息
            if (GenConstants.TYPE_ENUM.equals(column.getJavaType())) {
                if (StringUtils.isBlank(column.getEnumFullName())) {
                    throw new CustomException("请为枚举类型字段填写枚举类全限定名");
                }
                try {
                    // 设置枚举名称列表
                    Class<?> enumClass = Class.forName(column.getEnumFullName());
                    Object[] enumConstants = enumClass.getEnumConstants();
                    if (enumConstants != null) {
                        List<String> nameList = new ArrayList<>();
                        for (Object enumConstant : enumConstants) {
                            Enum enumobj = (Enum) enumConstant;
                            nameList.add(enumobj.name());
                        }
                        column.setEnumList(nameList);
                    }
                    // 设置枚举简单名
                    String[] strArr = StringUtils.split(column.getEnumFullName(), ".");
                    column.setEnumSimpleName(strArr[strArr.length - 1]);
                } catch (Exception e) {
                    log.error("---获取枚举类异常, 枚举类全限定名={}---", column.getEnumFullName(), e);
                    throw new CustomException("获取枚举类异常", e);
                }
            }
            // 为布尔类型补全信息
            if (GenConstants.TYPE_BOOLEAN.equals(column.getJavaType())) {
                try {
                    Map<String, String> booleanDescMap = new HashMap<>();
                    String[] convertSource = column.readConverterExp().split(",");
                    // 如未分隔成功，则尝试取中文逗号分隔
                    if (convertSource.length < 2) {
                        convertSource = column.readConverterExp().split("，");
                    }
                    for (String item : convertSource) {
                        String[] itemArray = item.split("=");
                        booleanDescMap.put(itemArray[0], itemArray[1]);
                    }
                    column.setBooleanDescMap(booleanDescMap);
                } catch (Exception e) {
                    log.error("---为布尔类型补全信息异常, 字段名={}---", column.getJavaField(), e);
                    throw new CustomException("为布尔类型补全信息异常", e);
                }
            }
        }
    }
}
