package com.airboot.project.tool.gen.model.entity;

import com.airboot.common.model.entity.BaseEntity;
import com.airboot.project.tool.gen.model.enums.TplCategoryEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务表 gen_table
 *
 * @author airboot
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("gen_table")
public class GenTable extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 表名称
     */
    @NotBlank(message = "表名称不能为空")
    private String tableName;
    
    /**
     * 表描述
     */
    @NotBlank(message = "表描述不能为空")
    private String tableComment;
    
    /**
     * 实体类名称(首字母大写)
     */
    @NotBlank(message = "实体类名称不能为空")
    private String className;
    
    /**
     * 使用的模板（crud单表操作 tree树表操作）
     */
    private TplCategoryEnum tplCategory;
    
    /**
     * 生成包路径
     */
    @NotBlank(message = "生成包路径不能为空")
    private String packageName;
    
    /**
     * 生成模块名
     */
    @NotBlank(message = "生成模块名不能为空")
    private String moduleName;
    
    /**
     * 生成业务名
     */
    @NotBlank(message = "生成业务名不能为空")
    private String businessName;
    
    /**
     * 生成功能名
     */
    @NotBlank(message = "生成功能名不能为空")
    private String functionName;
    
    /**
     * 生成作者
     */
    @NotBlank(message = "作者不能为空")
    private String functionAuthor;
    
    /**
     * 主键信息
     */
    @TableField(exist = false)
    private GenTableColumn pkColumn;
    
    /**
     * 表列信息
     */
    @Valid
    @TableField(exist = false)
    private List<GenTableColumn> columns;
    
    /**
     * 其它生成选项
     */
    private String options;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 树编码字段
     */
    @TableField(exist = false)
    private String treeCode;
    
    /**
     * 树父编码字段
     */
    @TableField(exist = false)
    private String treeParentCode;
    
    /**
     * 树名称字段
     */
    @TableField(exist = false)
    private String treeName;
    
    /**
     * 上级菜单ID字段
     */
    @TableField(exist = false)
    private String parentMenuId;
    
    /**
     * 上级菜单名称字段
     */
    @TableField(exist = false)
    private String parentMenuName;
    
    /**
     * 是否自动生成Mybatis ResultMap
     */
    @TableField(exist = false)
    private Boolean autoResultMap;
    
    /**
     * 是否需要生成Service的Interface，如果业务不复杂且确定未来不会用到多态，则可以不生成Interface，直接使用Service，方便应对需求变更
     */
    @TableField(exist = false)
    private Boolean interfaceService;
    
    /**
     * 是否使用前端全局$enums枚举变量来配置下拉菜单等组件，如开启使用，请确保你在系统中已建好枚举，并配置到了Constants.FRONT_SHOW_ENUMS中
     */
    @TableField(exist = false)
    private Boolean globalEnums;
    
    /**
     * 请求参数
     */
    @TableField(exist = false)
    private Map<String, Object> params;
    
    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }
    
}
