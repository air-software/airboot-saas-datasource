package com.airboot.project.monitor.service;

import com.airboot.common.security.LoginUser;
import com.airboot.project.monitor.model.entity.SysUserOnline;

/**
 * 在线用户 服务层
 *
 * @author airboot
 */
public interface ISysUserOnlineService {
    
    /**
     * 通过登录地址查询信息
     *
     * @param ipaddr 登录地址
     * @param user   用户信息
     * @return 在线用户信息
     */
    SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user);
    
    /**
     * 通过登录账号查询信息
     *
     * @param account 登录账号
     * @param user    用户信息
     * @return 在线用户信息
     */
    SysUserOnline selectOnlineByAccount(String account, LoginUser user);
    
    /**
     * 通过登录地址/登录账号查询信息
     *
     * @param ipaddr  登录地址
     * @param account 登录账号
     * @param user    用户信息
     * @return 在线用户信息
     */
    SysUserOnline selectOnlineByInfo(String ipaddr, String account, LoginUser user);
    
    /**
     * 设置在线用户信息
     *
     * @param user 用户信息
     * @return 在线用户
     */
    SysUserOnline loginUserToUserOnline(LoginUser user);
    
    /**
     * 根据用户ID强退用户
     *
     * @param userId 用户ID
     */
    void forceLogoutByUserId(Long userId);
}
