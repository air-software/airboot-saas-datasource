package com.airboot.project.monitor.controller;

import com.airboot.common.component.BaseController;
import com.airboot.common.core.aspectj.lang.annotation.Log;
import com.airboot.common.core.constant.Constants;
import com.airboot.common.core.redis.RedisCache;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.common.model.vo.AjaxResult;
import com.airboot.common.security.LoginUser;
import com.airboot.common.security.annotation.PreAuthorize;
import com.airboot.common.security.service.TokenService;
import com.airboot.project.monitor.model.entity.SysUserOnline;
import com.airboot.project.monitor.model.enums.OperationTypeEnum;
import com.airboot.project.monitor.service.ISysUserOnlineService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 在线用户监控
 *
 * @author airboot
 */
@RestController
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController {
    
    @Resource
    private ISysUserOnlineService userOnlineService;
    
    @Resource
    private TokenService tokenService;
    
    @Resource
    private RedisCache redisCache;
    
    @PreAuthorize("monitor:online:page")
    @GetMapping("/page")
    public AjaxResult list(String ipaddr, String account) {
        Collection<String> keys = redisCache.keys(Constants.REDIS_LOGIN_KEY + "*");
        List<SysUserOnline> userOnlineList = new ArrayList<>();
        for (String key : keys) {
            LoginUser user = redisCache.getCacheObject(key);
            if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(account)) {
                if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(account, user.getAccount())) {
                    userOnlineList.add(userOnlineService.selectOnlineByInfo(ipaddr, account, user));
                }
            } else if (StringUtils.isNotEmpty(ipaddr)) {
                if (StringUtils.equals(ipaddr, user.getIpaddr())) {
                    userOnlineList.add(userOnlineService.selectOnlineByIpaddr(ipaddr, user));
                }
            } else if (StringUtils.isNotEmpty(account) && StringUtils.isNotNull(user.getUser())) {
                if (StringUtils.equals(account, user.getAccount())) {
                    userOnlineList.add(userOnlineService.selectOnlineByAccount(account, user));
                }
            } else {
                userOnlineList.add(userOnlineService.loginUserToUserOnline(user));
            }
        }
        Collections.reverse(userOnlineList);
        userOnlineList.removeAll(Collections.singleton(null));
        IPage<SysUserOnline> page = new Page<>();
        page.setRecords(userOnlineList);
        page.setTotal(userOnlineList.size());
        return success(page);
    }
    
    /**
     * 强退用户
     */
    @PreAuthorize("monitor:online:forceLogout")
    @Log(title = "在线用户", operationType = OperationTypeEnum.强退)
    @DeleteMapping("/{userKey}/{uuid}")
    public AjaxResult forceLogout(@PathVariable String userKey, @PathVariable String uuid) {
        tokenService.delLoginUser(userKey, uuid);
        return success();
    }
}
