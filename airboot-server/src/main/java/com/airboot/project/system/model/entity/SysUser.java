package com.airboot.project.system.model.entity;

import com.airboot.common.core.aspectj.lang.annotation.Excel;
import com.airboot.common.core.aspectj.lang.annotation.Excel.Type;
import com.airboot.common.core.aspectj.lang.annotation.Excels;
import com.airboot.common.core.constant.Constants;
import com.airboot.common.core.utils.DateUtils;
import com.airboot.common.model.entity.BaseEntity;
import com.airboot.common.model.enums.StatusEnum;
import com.airboot.common.security.LoginUser;
import com.airboot.common.security.LoginUserContextHolder;
import com.airboot.project.system.model.enums.CardTypeEnum;
import com.airboot.project.system.model.enums.GenderEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 用户对象 sys_user
 *
 * @author airboot
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class SysUser extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 租户ID，业务逻辑中传递使用
     */
    @TableField(exist = false)
    private Long tenantId;
    
    /**
     * 部门ID
     */
    @Excel(name = "部门编号", type = Type.IMPORT)
    @NotNull
    private Long deptId;
    
    /**
     * 手机号码
     */
    @Excel(name = "手机号码")
    @NotBlank(message = "手机号码不能为空")
    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    private String mobile;
    
    /**
     * 用户名
     */
    @Excel(name = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 0, max = 30, message = "用户名长度不能超过30个字符")
    private String username;
    
    /**
     * 用户姓名
     */
    @Excel(name = "姓名")
    @Size(min = 0, max = 30, message = "姓名长度不能超过30个字符")
    private String personName;
    
    /**
     * 用户邮箱
     */
    @Excel(name = "用户邮箱（选填）")
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    private String email;
    
    /**
     * 用户性别
     */
    @Excel(name = "用户性别（男/女）")
    private GenderEnum gender;
    
    /**
     * 用户头像
     */
    private String avatar;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 证件号码
     */
    private String idCard;
    
    /**
     * 证件类型
     */
    private CardTypeEnum cardType;
    
    /**
     * 用户昵称
     */
    private String nickname;
    
    /**
     * 帐号状态（0停用 1正常）
     */
    @Excel(name = "帐号状态", type = Type.EXPORT)
    private StatusEnum status;
    
    /**
     * 最后登录IP
     */
    @Excel(name = "最后登录IP", type = Type.EXPORT)
    private String loginIp;
    
    /**
     * 最后登录地点
     */
    @Excel(name = "最后登录地点", type = Type.EXPORT)
    private String loginLocation;
    
    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT, locale = "zh", timezone = "GMT+8")
    @Excel(name = "最后登录时间", width = 30, dateFormat = DateUtils.DATETIME_FORMAT, type = Type.EXPORT)
    private Date loginDate;
    
    /**
     * 部门对象
     */
    @Excels({
            @Excel(name = "部门名称", targetAttr = "deptName", type = Type.EXPORT),
            @Excel(name = "部门负责人", targetAttr = "leader", type = Type.EXPORT)
    })
    @TableField(exist = false)
    private SysDept dept;
    
    /**
     * 角色对象
     */
    @TableField(exist = false)
    private List<SysRole> roles;
    
    /**
     * 角色组
     */
    @TableField(exist = false)
    private Long[] roleIds;
    
    /**
     * 岗位组
     */
    @TableField(exist = false)
    private Long[] postIds;
    
    public SysUser(Long userId) {
        this.setId(userId);
    }
    
    @JsonIgnore
    @JsonProperty
    public String getPassword() {
        return password;
    }
    
    /**
     * 是否为超级租户管理员
     */
    public boolean isTenantAdmin() {
        return this.getTenantId() == null ? isTenantAdmin(this.getId()) : isTenantAdmin(this.getTenantId(), this.getId());
    }
    
    /**
     * 如果所属租户为管理平台，且用户ID为1，则是超级租户管理员
     */
    public static boolean isTenantAdmin(Long userId) {
        LoginUser loginUser = LoginUserContextHolder.getLoginUser();
        return Constants.ADMIN_TENANT_ID.equals(loginUser.getTenantId()) && Constants.TENANT_ADMIN_USER_ID.equals(userId);
    }
    
    public static boolean isTenantAdmin(Long tenantId, Long userId) {
        return Constants.ADMIN_TENANT_ID.equals(tenantId) && Constants.TENANT_ADMIN_USER_ID.equals(userId);
    }
    
    /**
     * 是否为管理员
     * 如果是超级租户管理员，或所属角色列表中某一个的roleType为管理员，则认为是管理员
     */
    public boolean isAdmin() {
        return this.isTenantAdmin() || isAdmin(this);
    }
    
    public static boolean isAdmin(SysUser user) {
        if (CollectionUtils.isEmpty(user.getRoles())) {
            return false;
        }
        for (SysRole role : user.getRoles()) {
            if (role.isAdmin()) {
                return true;
            }
        }
        return false;
    }
    
}
