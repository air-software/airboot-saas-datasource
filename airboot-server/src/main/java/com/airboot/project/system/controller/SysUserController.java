package com.airboot.project.system.controller;

import com.airboot.common.component.BaseController;
import com.airboot.common.core.aspectj.lang.annotation.Log;
import com.airboot.common.core.exception.CustomException;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.common.core.utils.poi.ExcelUtil;
import com.airboot.common.core.utils.security.Md5Utils;
import com.airboot.common.model.enums.StatusEnum;
import com.airboot.common.model.vo.AjaxResult;
import com.airboot.common.security.LoginUserContextHolder;
import com.airboot.common.security.annotation.PreAuthorize;
import com.airboot.project.monitor.model.enums.OperationTypeEnum;
import com.airboot.project.monitor.service.ISysUserOnlineService;
import com.airboot.project.system.model.entity.SysRole;
import com.airboot.project.system.model.entity.SysUser;
import com.airboot.project.system.model.vo.SearchSysUserVO;
import com.airboot.project.system.service.ISysPostService;
import com.airboot.project.system.service.ISysRoleService;
import com.airboot.project.system.service.ISysUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author airboot
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
    
    @Resource
    private ISysUserService userService;
    
    @Resource
    private ISysRoleService roleService;
    
    @Resource
    private ISysPostService postService;
    
    @Resource
    private ISysUserOnlineService userOnlineService;
    
    /**
     * 获取用户分页
     */
    @PreAuthorize("system:user:page")
    @GetMapping("/page")
    public AjaxResult page(SearchSysUserVO search) {
        IPage<SysUser> page = userService.getPage(search);
        return success(page);
    }
    
    /**
     * 获取用户列表
     */
    @PreAuthorize("system:user:page")
    @GetMapping("/list")
    public AjaxResult list(SearchSysUserVO search) {
        List<SysUser> list = userService.getList(search);
        return success(list);
    }
    
    @Log(title = "用户管理", operationType = OperationTypeEnum.导出)
    @PreAuthorize("system:user:export")
    @GetMapping("/export")
    public AjaxResult export(SearchSysUserVO search) {
        List<SysUser> list = userService.getList(search);
        ExcelUtil<SysUser> util = new ExcelUtil<>(SysUser.class);
        return util.exportExcel(list, "用户数据");
    }
    
    @Log(title = "用户管理", operationType = OperationTypeEnum.导入)
    @PreAuthorize("system:user:import")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String message = userService.importUser(userList, updateSupport);
        return success(message);
    }
    
    @GetMapping("/importTemplate")
    public AjaxResult importTemplate() {
        ExcelUtil<SysUser> util = new ExcelUtil<>(SysUser.class);
        return util.importTemplateExcel("用户数据");
    }
    
    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("system:user:query")
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        Map<String, Object> dataMap = new HashMap<>();
        
        // 只有管理员可以为其他人分配管理员角色
        List<SysRole> roles = roleService.getAll();
        boolean isAdmin = LoginUserContextHolder.getLoginUser().getUser().isAdmin();
        final Long[] adminRoleId = {1L};
        dataMap.put("roles", isAdmin ? roles : roles.stream().filter(r -> {
            // 如果不是管理员，则去掉管理员角色
            if (r.isAdmin()) {
                adminRoleId[0] = r.getId();
                return false;
            }
            return true;
        }).collect(Collectors.toList()));
        
        dataMap.put("posts", postService.getAll());
        if (StringUtils.isNotNull(userId)) {
            dataMap.put("user", userService.getById(userId));
            dataMap.put("postIds", postService.getIdListByUserId(userId));
            
            List<Long> roleIds = roleService.getIdListByUserId(userId);
            // 如果不是管理员，则在已选ID中去掉管理员角色ID
            dataMap.put("roleIds", isAdmin ? roleIds : roleIds.stream().filter(roleId -> !roleId.equals(adminRoleId[0])).collect(Collectors.toList()));
        }
        return success(dataMap);
    }
    
    /**
     * 新增用户
     */
    @PreAuthorize("system:user:add")
    @Log(title = "用户管理", operationType = OperationTypeEnum.新增)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        if (StringUtils.isBlank(user.getPassword()) || user.getPassword().length() < 6 || user.getPassword().length() > 50) {
            throw new CustomException("密码长度必须在6-50位之间");
        }
        userService.validateUser(user, false);
        
        user.setPassword(Md5Utils.encryptPassword(user.getPassword()));
        return toAjax(userService.save(user));
    }
    
    /**
     * 修改用户
     */
    @PreAuthorize("system:user:edit")
    @Log(title = "用户管理", operationType = OperationTypeEnum.修改)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.validateUser(user, true);

        // 防止前端恶意更新密码
        user.setPassword(null);
        userService.update(user);
        
        // 如果本次操作是停用用户，则直接强退该用户
        if (StatusEnum.停用.equals(user.getStatus())) {
            userOnlineService.forceLogoutByUserId(user.getId());
        }
        
        return success();
    }
    
    /**
     * 删除用户
     */
    @PreAuthorize("system:user:remove")
    @Log(title = "用户管理", operationType = OperationTypeEnum.删除)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds) {
        int deleteResult = userService.deleteByIds(userIds);
    
        // 强退所有已删除的用户
        if (deleteResult > 0) {
            for (Long userId : userIds) {
                userOnlineService.forceLogoutByUserId(userId);
            }
        }
        
        return toAjax(deleteResult);
    }
    
    /**
     * 重置密码
     */
    @PreAuthorize("system:user:edit")
    @Log(title = "用户管理", operationType = OperationTypeEnum.修改)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        if (StringUtils.isBlank(user.getPassword()) || user.getPassword().length() < 6 || user.getPassword().length() > 50) {
            throw new CustomException("密码长度必须在6-50位之间");
        }
        
        user.setPassword(Md5Utils.encryptPassword(user.getPassword()));
        // 新建一个user实例来更新，防止更新到其他字段
        userService.updateUserProfile(SysUser.builder()
            .id(user.getId())
            .password(user.getPassword())
            .build());
        
        return success();
    }
    
    /**
     * 状态修改
     */
    @PreAuthorize("system:user:edit")
    @Log(title = "用户管理", operationType = OperationTypeEnum.修改)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        // 新建一个user实例来更新，防止更新到其他字段
        SysUser updateUser = SysUser.builder()
            .id(user.getId())
            .status(user.getStatus())
            .build();
        userService.updateUserProfile(updateUser);
        
        // 如果本次操作是停用用户，则直接强退该用户
        if (StatusEnum.停用.equals(user.getStatus())) {
            userOnlineService.forceLogoutByUserId(user.getId());
        }
        
        return success();
    }
}
