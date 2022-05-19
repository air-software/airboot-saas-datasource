package com.airboot.project.tool.gen.model.entity;

import com.airboot.common.core.utils.StringUtils;
import com.airboot.common.model.entity.BaseEntity;
import com.airboot.project.tool.gen.model.enums.HtmlTypeEnum;
import com.airboot.project.tool.gen.model.enums.QueryTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 代码生成业务字段表 gen_table_column
 *
 * @author airboot
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("gen_table_column")
public class GenTableColumn extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 归属表编号
     */
    private Long tableId;
    
    /**
     * 列名称
     */
    private String columnName;
    
    /**
     * 列描述
     */
    private String columnComment;
    
    /**
     * 列类型
     */
    private String columnType;
    
    /**
     * JAVA类型
     */
    private String javaType;
    
    /**
     * JAVA字段名
     */
    @NotBlank(message = "Java属性不能为空")
    private String javaField;
    
    /**
     * 是否主键（false=否,true=是）
     */
    private boolean primaryKey;
    
    /**
     * 是否自增（false=否,true=是）
     */
    private boolean incremental;
    
    /**
     * 是否必填（false=否,true=是）
     */
    private boolean required;
    
    /**
     * 是否为插入字段（false=否,true=是）
     */
    private boolean insertable;
    
    /**
     * 是否编辑字段（false=否,true=是）
     */
    private boolean edit;
    
    /**
     * 是否列表字段（false=否,true=是）
     */
    private boolean list;
    
    /**
     * 是否为导出字段（false=否,true=是）
     */
    private boolean excelExport;
    
    /**
     * 是否为导入字段（false=否,true=是）
     */
    private boolean excelImport;
    
    /**
     * 是否查询字段（false=否,true=是）
     */
    private boolean query;
    
    /**
     * 查询方式（EQ等于、NE不等于、GT大于、GTE大于等于、LT小于、LTE小于等于、LIKE模糊、BETWEEN范围）
     */
    private QueryTypeEnum queryType;
    
    /**
     * 显示类型（input文本框、textarea文本域、select下拉框、checkbox复选框、radio单选框、datetime日期控件）
     */
    private HtmlTypeEnum htmlType;
    
    /**
     * 枚举类全限定名
     */
    private String enumFullName;
    
    /**
     * 枚举类简单名
     */
    @TableField(exist = false)
    private String enumSimpleName;
    
    /**
     * 枚举列表
     */
    @Builder.Default
    @TableField(exist = false)
    private List<String> enumList = new ArrayList<>();
    
    /**
     * 布尔值描述Map
     */
    @TableField(exist = false)
    private Map<String, String> booleanDescMap;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 是否实体父类字段
     */
    public boolean isSuperColumn() {
        return isSuperColumn(this.javaField);
    }
    
    public static boolean isSuperColumn(String javaField) {
        return StringUtils.equalsAnyIgnoreCase(javaField,
                // BaseEntity
                "id", "creatorId", "creatorInfo", "createTime", "updaterId", "updaterInfo", "updateTime", "extJson", "dataScope", "deleted", "version",
                // TreeEntity
                "parentName", "parentId", "orderNum", "ancestors");
    }
    
    /**
     * 是否SearchVO父类字段
     */
    public boolean isSearchSuperColumn() {
        return isSearchSuperColumn(this.javaField);
    }
    
    public static boolean isSearchSuperColumn(String javaField) {
        return StringUtils.equalsAnyIgnoreCase(javaField,
            // BaseSearchVO
            "id", "createTime", "updateTime", "beginTime", "endTime", "dataScopeSql", "status",
            // Page
            "total", "size", "current", "records", "orders");
    }
    
    public boolean isUsableColumn() {
        return isUsableColumn(javaField);
    }
    
    public static boolean isUsableColumn(String javaField) {
        // isSuperColumn()中的名单用于避免生成多余Domain属性，若某些属性在生成页面时需要用到不能忽略，则放在此处白名单
        return StringUtils.equalsAnyIgnoreCase(javaField, "parentId", "orderNum");
    }
    
    public String readConverterExp() {
        return StringUtils.substringBetween(this.columnComment, "（", "）");
    }
}
