package com.airboot.common.security.interceptor;

import com.airboot.common.core.config.ProjectConfig;
import com.airboot.common.core.config.properties.AppProp;
import com.airboot.common.core.constant.HttpStatus;
import com.airboot.common.core.exception.CustomException;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.common.security.LoginUser;
import com.airboot.common.security.LoginUserContextHolder;
import com.airboot.common.security.service.TokenService;
import com.airsoftware.saas.datasource.context.SaaSDataSource;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 *
 * @author airboot
 */
@Slf4j
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    
    @Resource
    private TokenService tokenService;
    
    @Resource
    private ProjectConfig projectConfig;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果是跨域的前置OPTIONS请求，并且是非生产环境，则允许通过
        if (RequestMethod.OPTIONS.name().equals(request.getMethod()) && AppProp.NOT_PROD_ENV) {
            return true;
        }
    
        log.info("---请求URI={}, 请求参数={}---", request.getRequestURI(), JSON.toJSONString(request.getParameterMap()));
        
        LoginUser loginUser = tokenService.getLoginUser(request);
        // 如果是唯一登录，并且用户已被踢出
        if (projectConfig.isUniqueLogin() && loginUser.isKickout()) {
            tokenService.delLoginUser(loginUser.getUserKey(), loginUser.getUuid());
            throw new CustomException("用户已被踢出，请确认是否本人操作", HttpStatus.KICK_OUT);
        }
        
        // 如果是超级租户管理员，则可以看到其他租户的数据（取header中的tenantId）
        if (loginUser.getUser().isTenantAdmin() && StringUtils.isNotBlank(request.getHeader("tenantId"))) {
            SaaSDataSource.switchTo(request.getHeader("tenantId"));
        } else {
            SaaSDataSource.switchTo(loginUser.getTenantId());
        }
        
        // 设置上下文
        LoginUserContextHolder.setLoginUser(loginUser);
        // 校验token有效期
        tokenService.verifyToken(loginUser);
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        LoginUserContextHolder.destroy();
        SaaSDataSource.removeAll();
    }
    
}
