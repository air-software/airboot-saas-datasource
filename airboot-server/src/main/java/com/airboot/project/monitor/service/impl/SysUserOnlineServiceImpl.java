package com.airboot.project.monitor.service.impl;

import com.airboot.common.core.constant.Constants;
import com.airboot.common.core.redis.RedisCache;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.common.model.enums.DeviceEnum;
import com.airboot.common.security.LoginUser;
import com.airboot.common.security.service.TokenService;
import com.airboot.project.monitor.model.entity.SysUserOnline;
import com.airboot.project.monitor.service.ISysUserOnlineService;
import com.airboot.project.system.model.entity.SysUser;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * 在线用户 服务层处理
 *
 * @author airboot
 */
@Service
public class SysUserOnlineServiceImpl implements ISysUserOnlineService {
    
    @Resource
    private TokenService tokenService;
    
    @Resource
    private RedisCache redisCache;
    
    /**
     * 通过登录地址查询信息
     *
     * @param ipaddr 登录地址
     * @param user   用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user) {
        if (StringUtils.equals(ipaddr, user.getIpaddr())) {
            return loginUserToUserOnline(user);
        }
        return null;
    }
    
    /**
     * 通过登录账号查询信息
     *
     * @param account  登录账号
     * @param user     用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByAccount(String account, LoginUser user) {
        if (StringUtils.equals(account, user.getAccount())) {
            return loginUserToUserOnline(user);
        }
        return null;
    }
    
    /**
     * 通过登录地址/登录账号查询信息
     *
     * @param ipaddr   登录地址
     * @param account  登录账号
     * @param user     用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByInfo(String ipaddr, String account, LoginUser user) {
        if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(account, user.getAccount())) {
            return loginUserToUserOnline(user);
        }
        return null;
    }
    
    /**
     * 设置在线用户信息
     *
     * @param user 用户信息
     * @return 在线用户
     */
    @Override
    public SysUserOnline loginUserToUserOnline(LoginUser user) {
        if (StringUtils.isNull(user) || StringUtils.isNull(user.getUser())) {
            return null;
        }
        SysUserOnline sysUserOnline = new SysUserOnline();
        sysUserOnline.setUuid(user.getUuid());
        sysUserOnline.setUserKey(user.getUserKey());
        sysUserOnline.setAccount(user.getAccount());
        sysUserOnline.setUsername(user.getUsername());
        sysUserOnline.setIpaddr(user.getIpaddr());
        sysUserOnline.setLoginLocation(user.getLoginLocation());
        sysUserOnline.setDevice(user.getDevice());
        sysUserOnline.setBrowser(user.getBrowser());
        sysUserOnline.setOs(user.getOs());
        sysUserOnline.setLoginTime(user.getLoginTime());
        if (StringUtils.isNotNull(user.getUser().getDept())) {
            sysUserOnline.setDeptName(user.getUser().getDept().getDeptName());
        }
        return sysUserOnline;
    }
    
    /**
     * 根据用户ID强退用户
     *
     * @param userId 用户ID
     */
    @Override
    public void forceLogoutByUserId(Long userId) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(new SysUser(userId));
        // 遍历此用户可能登录的设备
        for (DeviceEnum device : DeviceEnum.values()) {
            loginUser.setDevice(device);
            String userKey = tokenService.genUserKey(loginUser);
            // 获取此设备下此用户可能存在的redisKey，并批量删除
            Collection<String> keys = redisCache.keys(Constants.REDIS_LOGIN_KEY + userKey + "," + "*");
            if (CollectionUtils.isNotEmpty(keys)) {
                redisCache.deleteObject(keys);
            }
        }
    }
}
