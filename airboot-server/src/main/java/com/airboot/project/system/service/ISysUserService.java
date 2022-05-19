package com.airboot.project.system.service;

import com.airboot.project.system.model.entity.SysUser;
import com.airboot.project.system.model.vo.SearchSysUserVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 用户 业务层
 *
 * @author airboot
 */
public interface ISysUserService {
    
    /**
     * 查询用户分页
     *
     * @param search 查询条件
     * @return 分页结果
     */
    IPage<SysUser> getPage(SearchSysUserVO search);
    
    /**
     * 查询用户列表
     *
     * @param search 查询条件
     * @return 用户列表
     */
    List<SysUser> getList(SearchSysUserVO search);
    
    /**
     * 通过登录账号查询用户
     *
     * @param account 用户登录账号
     * @return 用户对象信息
     */
    SysUser getByAccount(String account);
    
    /**
     * 查询所有正常用户
     */
    List<SysUser> getAllNormalList();
    
    /**
     * 查询正常用户
     */
    SysUser getNormalOne(Long id);
    
    /**
     * 根据手机号查询正常用户
     */
    SysUser getNormalOneByMobile(String mobile);
    
    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    SysUser getById(Long userId);
    
    /**
     * 通过用户ID查询用户，并携带密码
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    SysUser getByIdWithPwd(Long userId);
    
    /**
     * 根据用户ID查询用户所属角色组
     *
     * @param userId 用户ID
     * @return 结果
     */
    String getUserRoleGroup(Long userId);
    
    /**
     * 根据用户ID查询用户所属岗位组
     *
     * @param userId 用户ID
     * @return 结果
     */
    String getUserPostGroup(Long userId);
    
    /**
     * 校验用户
     *
     * @param user 用户信息
     * @param isUpdate 是否更新
     */
    void validateUser(SysUser user, boolean isUpdate);
    
    /**
     * 校验用户名是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean checkUsernameUnique(SysUser user);
    
    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean checkMobileUnique(SysUser user);
    
    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean checkEmailUnique(SysUser user);
    
    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    void checkUserAllowed(SysUser user);
    
    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int save(SysUser user);
    
    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    void update(SysUser user);
    
    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    void updateUserProfile(SysUser user);
    
    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    int deleteById(Long userId);
    
    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    int deleteByIds(Long[] userIds);
    
    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param updateSupport   是否支持更新，如果已存在，则进行更新数据
     * @return 结果
     */
    String importUser(List<SysUser> userList, boolean updateSupport);
    
}
