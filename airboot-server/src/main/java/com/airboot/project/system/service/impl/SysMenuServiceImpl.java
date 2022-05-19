package com.airboot.project.system.service.impl;

import com.airboot.common.core.constant.Constants;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.common.model.entity.TreeSelect;
import com.airboot.project.system.mapper.SysMenuMapper;
import com.airboot.project.system.mapper.relation.SysRoleMenuMapper;
import com.airboot.project.system.model.entity.SysMenu;
import com.airboot.project.system.model.entity.SysUser;
import com.airboot.project.system.model.enums.MenuTypeEnum;
import com.airboot.project.system.model.vo.MetaVO;
import com.airboot.project.system.model.vo.RouterVO;
import com.airboot.project.system.model.vo.SearchSysMenuVO;
import com.airboot.project.system.service.ISysMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单 业务层处理
 *
 * @author airboot
 */
@Service
public class SysMenuServiceImpl implements ISysMenuService {
    
    public static final String PREMISSION_STRING = "perms[\"{0}\"]";
    
    @Resource
    private SysMenuMapper menuMapper;
    
    @Resource
    private SysRoleMenuMapper roleMenuMapper;
    
    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> getList(Long userId) {
        return getList(new SearchSysMenuVO(), userId);
    }
    
    /**
     * 查询系统菜单列表
     *
     * @param search 查询条件
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> getList(SearchSysMenuVO search, Long userId) {
        List<SysMenu> menuList = new ArrayList<>();
        // 超级租户管理员显示所有菜单信息
        if (SysUser.isTenantAdmin(userId)) {
            menuList = menuMapper.findList(search);
        } else {
            // 从租户库中查询菜单ID列表
            List<Long> menuIdList = roleMenuMapper.findMenuIdListByUserId(userId);
            if (CollectionUtils.isNotEmpty(menuIdList)) {
                search.setIdList(menuIdList);
                menuList = menuMapper.findList(search);
            }
        }
        return menuList;
    }
    
    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> getMenuPermsByUserId(Long userId) {
        // 从租户库中查询菜单ID列表
        List<Long> menuIdList = roleMenuMapper.findMenuIdListByUserIdAndNormalRole(userId);
        List<String> perms = CollectionUtils.isNotEmpty(menuIdList) ? menuMapper.findMenuPermsInIdList(menuIdList) : new ArrayList<>();
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }
    
    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> getMenuTreeByUserId(Long userId) {
        List<SysMenu> menus = new ArrayList<>();
        // 超级租户管理员显示所有菜单信息
        if (SysUser.isTenantAdmin(userId)) {
            menus = menuMapper.findMenuTreeAll();
        } else {
            // 从租户库中查询菜单ID列表
            List<Long> menuIdList = roleMenuMapper.findMenuIdListByUserIdAndNormalRole(userId);
            if (CollectionUtils.isNotEmpty(menuIdList)) {
                SearchSysMenuVO search = new SearchSysMenuVO();
                search.setIdList(menuIdList);
                search.setQueryType("tree");
                menus = menuMapper.findList(search);
            }
        }
        return getChildPerms(menus, 0L);
    }
    
    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    @Override
    public List<Long> getIdListByRoleId(Long roleId) {
        // 从租户库中查询菜单ID列表
        List<Long> menuIdList = roleMenuMapper.findMenuIdListByRoleId(roleId);
        return CollectionUtils.isNotEmpty(menuIdList) ? menuMapper.findCheckedIdList(menuIdList) : new ArrayList<>();
    }
    
    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVO> buildMenus(List<SysMenu> menus) {
        List<RouterVO> routers = new LinkedList<>();
        for (SysMenu menu : menus) {
            RouterVO router = new RouterVO();
            router.setHidden(menu.getHidden());
            router.setName(StringUtils.capitalize(menu.getPath()));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setMeta(new MetaVO(menu.getMenuName(), menu.getIcon()));
            List<SysMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && MenuTypeEnum.目录.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMeunFrame(menu)) {
                List<RouterVO> childrenList = new ArrayList<>();
                RouterVO children = new RouterVO();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(new MetaVO(menu.getMenuName(), menu.getIcon()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }
    
    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<>();
        for (SysMenu t : menus) {
            // 根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == 0) {
                recursionFn(menus, t);
                returnList.add(t);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }
    
    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus) {
        List<SysMenu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }
    
    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    @Override
    public SysMenu getById(Long menuId) {
        return menuMapper.selectById(menuId);
    }
    
    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean hasChildByMenuId(Long menuId) {
        int result = menuMapper.hasChildByMenuId(menuId);
        return result > 0;
    }
    
    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean checkMenuExistRole(Long menuId) {
        int result = roleMenuMapper.checkMenuExistRole(menuId);
        return result > 0;
    }
    
    /**
     * 保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public void saveOrUpdate(SysMenu menu) {
        menuMapper.saveOrUpdate(menu);
    }
    
    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public int deleteById(Long menuId) {
        return menuMapper.deleteById(menuId);
    }
    
    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public boolean checkMenuNameUnique(SysMenu menu) {
        if (StringUtils.isBlank(menu.getMenuName())) {
            return false;
        }
        Long menuId = StringUtils.isNull(menu.getId()) ? -1L : menu.getId();
        SysMenu info = menuMapper.getOne(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getMenuName, menu.getMenuName()).eq(SysMenu::getParentId, menu.getParentId()), false);
        return info == null || info.getId().equals(menuId);
    }
    
    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = menu.getPath();
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId() && MenuTypeEnum.目录.equals(menu.getMenuType())
                && !menu.getIframe()) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMeunFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }
    
    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenu menu) {
        String component = Constants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMeunFrame(menu)) {
            component = menu.getComponent();
        }
        return component;
    }
    
    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMeunFrame(SysMenu menu) {
        return menu.getParentId() == 0 && MenuTypeEnum.菜单.equals(menu.getMenuType())
                && !menu.getIframe();
    }
    
    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, Long parentId) {
        List<SysMenu> returnList = new ArrayList<>();
        for (SysMenu t : list) {
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId().equals(parentId)) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }
    
    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                // 判断是否有子节点
                for (SysMenu n : childList) {
                    recursionFn(list, n);
                }
            }
        }
    }
    
    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<>();
        for (SysMenu n : list) {
            if (n.getParentId().equals(t.getId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }
    
    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
    }
}
