package com.airboot.common.security.controller;

import com.airboot.common.core.manager.AsyncManager;
import com.airboot.common.core.manager.factory.AsyncFactory;
import com.airboot.common.core.utils.EnumUtil;
import com.airboot.common.core.utils.MessageUtils;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.common.model.enums.LoginResultEnum;
import com.airboot.common.model.vo.AjaxResult;
import com.airboot.common.security.LoginBody;
import com.airboot.common.security.LoginUser;
import com.airboot.common.security.LoginUserContextHolder;
import com.airboot.common.security.RecordLogininforVO;
import com.airboot.common.security.service.SysLoginService;
import com.airboot.common.security.service.SysPermissionService;
import com.airboot.common.security.service.TokenService;
import com.airboot.project.system.model.entity.SysMenu;
import com.airboot.project.system.model.entity.SysUser;
import com.airboot.project.system.model.enums.RoleTypeEnum;
import com.airboot.project.system.service.ISysMenuService;
import com.airboot.project.system.service.ISysTenantService;
import com.airsoftware.saas.datasource.context.SaaSDataSource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 登录验证
 *
 * @author airboot
 */
@RestController
public class SysLoginController {
    
    @Resource
    private SysLoginService loginService;
    
    @Resource
    private ISysMenuService menuService;
    
    @Resource
    private SysPermissionService permissionService;
    
    @Resource
    private ISysTenantService tenantService;
    
    @Resource
    private TokenService tokenService;
    
    /**
     * 登录方法
     *
     * @param loginBody 登录参数
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@Validated @RequestBody LoginBody loginBody) {
        // 切换至对应租户
        SaaSDataSource.switchTo(loginBody.getTenantId());
        // 生成令牌
        String token = loginService.login(loginBody).getToken();
        return AjaxResult.success(token);
    }
    
    /**
     * 登出
     */
    @PostMapping("/logout")
    public AjaxResult logout() {
        LoginUser loginUser = LoginUserContextHolder.getLoginUser();
        if (StringUtils.isNotNull(loginUser)) {
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getUserKey(), loginUser.getUuid());
            // 记录用户退出日志
            RecordLogininforVO recordLogininforVO = RecordLogininforVO.builder()
                .tenantId(loginUser.getTenantId())
                .userId(loginUser.getUserId())
                .account(loginUser.getAccount())
                .device(loginUser.getDevice())
                .loginResult(LoginResultEnum.退出成功)
                .msg(MessageUtils.message("user.logout.success"))
                .build();
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(recordLogininforVO));
        }
        return AjaxResult.success("退出成功");
    }
    
    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = LoginUserContextHolder.getLoginUser();
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<RoleTypeEnum> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("user", user);
        dataMap.put("roles", roles);
        dataMap.put("permissions", permissions);
        dataMap.put("tenant", tenantService.getById(loginUser.getTenantId()));
        dataMap.put("enums", EnumUtil.getAllNameList());
        return AjaxResult.success(dataMap);
    }
    
    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        LoginUser loginUser = LoginUserContextHolder.getLoginUser();
        // 用户信息
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.getMenuTreeByUserId(user.getId());
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
