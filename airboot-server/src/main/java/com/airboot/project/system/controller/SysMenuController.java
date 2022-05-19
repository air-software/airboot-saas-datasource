package com.airboot.project.system.controller;

import com.airboot.common.component.BaseController;
import com.airboot.common.core.aspectj.lang.annotation.Log;
import com.airboot.common.core.constant.Constants;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.common.model.vo.AjaxResult;
import com.airboot.common.security.LoginUser;
import com.airboot.common.security.LoginUserContextHolder;
import com.airboot.common.security.annotation.PreAuthorize;
import com.airboot.project.monitor.model.enums.OperationTypeEnum;
import com.airboot.project.system.model.entity.SysMenu;
import com.airboot.project.system.model.vo.SearchSysMenuVO;
import com.airboot.project.system.service.ISysMenuService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单信息
 *
 * @author airboot
 */
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {
    
    @Resource
    private ISysMenuService menuService;
    
    /**
     * 获取菜单列表
     */
    @PreAuthorize("system:menu:list")
    @GetMapping("/list")
    public AjaxResult list(SearchSysMenuVO search) {
        LoginUser loginUser = LoginUserContextHolder.getLoginUser();
        Long userId = loginUser.getUser().getId();
        List<SysMenu> menus = menuService.getList(search, userId);
        return success(menus);
    }
    
    /**
     * 根据菜单编号获取详细信息
     */
    @PreAuthorize("system:menu:query")
    @GetMapping(value = "/{menuId}")
    public AjaxResult getInfo(@PathVariable Long menuId) {
        return success(menuService.getById(menuId));
    }
    
    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SearchSysMenuVO search) {
        LoginUser loginUser = LoginUserContextHolder.getLoginUser();
        Long userId = loginUser.getUser().getId();
        List<SysMenu> menus = menuService.getList(search, userId);
        return success(menuService.buildMenuTreeSelect(menus));
    }
    
    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public AjaxResult roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
        LoginUser loginUser = LoginUserContextHolder.getLoginUser();
        List<SysMenu> menus = menuService.getList(loginUser.getUser().getId());
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("checkedKeys", menuService.getIdListByRoleId(roleId));
        dataMap.put("menus", menuService.buildMenuTreeSelect(menus));
        return success(dataMap);
    }
    
    /**
     * 新增菜单
     */
    @PreAuthorize("system:menu:add")
    @Log(title = "菜单管理", operationType = OperationTypeEnum.新增)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysMenu menu) {
        if (!menuService.checkMenuNameUnique(menu)) {
            return fail("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (menu.getIframe()
                && !StringUtils.startsWithAny(menu.getPath(), Constants.HTTP, Constants.HTTPS)) {
            return fail("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        menuService.saveOrUpdate(menu);
        return success();
    }
    
    /**
     * 修改菜单
     */
    @PreAuthorize("system:menu:edit")
    @Log(title = "菜单管理", operationType = OperationTypeEnum.修改)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysMenu menu) {
        if (!menuService.checkMenuNameUnique(menu)) {
            return fail("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (menu.getIframe()
                && !StringUtils.startsWithAny(menu.getPath(), Constants.HTTP, Constants.HTTPS)) {
            return fail("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (menu.getId().equals(menu.getParentId())) {
            return fail("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menuService.saveOrUpdate(menu);
        return success();
    }
    
    /**
     * 删除菜单
     */
    @PreAuthorize("system:menu:remove")
    @Log(title = "菜单管理", operationType = OperationTypeEnum.删除)
    @DeleteMapping("/{menuId}")
    public AjaxResult remove(@PathVariable("menuId") Long menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return fail("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return fail("菜单已分配,不允许删除");
        }
        return toAjax(menuService.deleteById(menuId));
    }
}
