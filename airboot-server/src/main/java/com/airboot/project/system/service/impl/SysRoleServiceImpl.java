package com.airboot.project.system.service.impl;

import com.airboot.common.core.aspectj.lang.annotation.DataScope;
import com.airboot.common.core.exception.CustomException;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.common.core.utils.spring.SpringUtils;
import com.airboot.common.security.LoginUserContextHolder;
import com.airboot.project.system.mapper.SysRoleMapper;
import com.airboot.project.system.mapper.relation.SysRoleDeptMapper;
import com.airboot.project.system.mapper.relation.SysRoleMenuMapper;
import com.airboot.project.system.mapper.relation.SysUserRoleMapper;
import com.airboot.project.system.model.entity.SysRole;
import com.airboot.project.system.model.entity.SysUser;
import com.airboot.project.system.model.entity.relation.SysRoleDept;
import com.airboot.project.system.model.entity.relation.SysRoleMenu;
import com.airboot.project.system.model.enums.RoleTypeEnum;
import com.airboot.project.system.model.vo.SearchSysRoleVO;
import com.airboot.project.system.service.ISysRoleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 角色 业务层处理
 *
 * @author airboot
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService {
    
    @Resource
    private SysRoleMapper roleMapper;
    
    @Resource
    private SysRoleMenuMapper roleMenuMapper;
    
    @Resource
    private SysUserRoleMapper userRoleMapper;
    
    @Resource
    private SysRoleDeptMapper roleDeptMapper;
    
    /**
     * 查询分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    @Override
    @DataScope(deptAlias = "d")
    public IPage<SysRole> getPage(SearchSysRoleVO search) {
        return roleMapper.findPage(search);
    }
    
    /**
     * 根据条件分页查询角色数据
     *
     * @param search 查询条件
     * @return 角色数据集合信息
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysRole> getList(SearchSysRoleVO search) {
        return roleMapper.findList(search);
    }
    
    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<RoleTypeEnum> getRoleTypeSetByUserId(Long userId) {
        List<SysRole> roleList = roleMapper.findListByUserId(userId);
        Set<RoleTypeEnum> roleTypeSet = new HashSet<>();
        for (SysRole role : roleList) {
            if (StringUtils.isNotNull(role)) {
                roleTypeSet.add(role.getRoleType());
            }
        }
        return roleTypeSet;
    }
    
    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<SysRole> getAll() {
        return SpringUtils.getAopProxy(this).getList(new SearchSysRoleVO());
    }
    
    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    @Override
    public List<Long> getIdListByUserId(Long userId) {
        return roleMapper.findIdListByUserId(userId);
    }
    
    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public SysRole getById(Long roleId) {
        return roleMapper.findById(roleId);
    }
    
    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleNameUnique(SysRole role) {
        if (StringUtils.isBlank(role.getRoleName())) {
            return false;
        }
        Long roleId = StringUtils.isNull(role.getId()) ? -1L : role.getId();
        SysRole info = roleMapper.checkRoleNameUnique(role.getRoleName());
        return info == null || info.getId().equals(roleId);
    }
    
    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRole role) {
        if (role.getId() == null) {
            throw new CustomException("未找到要操作的角色");
        }
        // 超级租户管理员可以操作所有租户的管理员，租户管理员只能操作自己租户内的管理员，其他角色不允许操作管理员
        SysUser user = LoginUserContextHolder.getLoginUser().getUser();
        // 从库里查询要操作的角色
        SysRole editRole = roleMapper.selectById(role.getId());
        
        // 如果不是管理员，却要操作管理员角色，则返回异常
        if (!user.isAdmin() && editRole.isAdmin()) {
            throw new CustomException("不允许操作管理员角色，如需修改请联系贵司管理员");
        }
        // 如果要操作的是管理员，但要修改的是它的roleType，则也不允许。只有超级租户管理员可以修改管理员的roleType。
        if (!user.isTenantAdmin() && editRole.isAdmin() && !RoleTypeEnum.管理员.equals(role.getRoleType())) {
            throw new CustomException("不允许修改管理员角色的权限字符");
        }
    }
    
    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int countUserRoleByRoleId(Long roleId) {
        return userRoleMapper.countByRoleId(roleId);
    }
    
    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int save(SysRole role) {
        // 新增角色信息
        roleMapper.saveOrUpdate(role);
        return insertRoleMenu(role);
    }
    
    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int update(SysRole role) {
        // 修改角色信息
        roleMapper.saveOrUpdate(role);
        // 删除角色与菜单关联
        roleMenuMapper.deleteByRoleId(role.getId());
        return insertRoleMenu(role);
    }
    
    /**
     * 修改角色状态
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public void updateStatus(SysRole role) {
        roleMapper.saveOrUpdate(role);
    }
    
    /**
     * 修改数据权限信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int authDataScope(SysRole role) {
        // 修改角色信息
        roleMapper.saveOrUpdate(role);
        // 删除角色与部门关联
        roleDeptMapper.deleteByRoleId(role.getId());
        // 新增角色和部门信息（数据权限）
        return insertRoleDept(role);
    }
    
    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    public int insertRoleMenu(SysRole role) {
        int rows = 1;
        // 新增用户与角色管理
        List<SysRoleMenu> list = new ArrayList<>();
        for (Long menuId : role.getMenuIds()) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0) {
            rows = roleMenuMapper.batchInsert(list);
        }
        return rows;
    }
    
    /**
     * 新增角色部门信息(数据权限)
     *
     * @param role 角色对象
     */
    public int insertRoleDept(SysRole role) {
        int rows = 1;
        // 新增角色与部门（数据权限）管理
        List<SysRoleDept> list = new ArrayList<>();
        for (Long deptId : role.getDeptIds()) {
            SysRoleDept rd = new SysRoleDept();
            rd.setRoleId(role.getId());
            rd.setDeptId(deptId);
            list.add(rd);
        }
        if (list.size() > 0) {
            rows = roleDeptMapper.batchInsert(list);
        }
        return rows;
    }
    
    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    public int deleteByIds(Long[] roleIds) {
        for (Long roleId : roleIds) {
            checkRoleAllowed(this.getById(roleId));
            SysRole role = getById(roleId);
            if (countUserRoleByRoleId(roleId) > 0) {
                throw new CustomException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        return roleMapper.deleteRoleByIds(roleIds);
    }
}
