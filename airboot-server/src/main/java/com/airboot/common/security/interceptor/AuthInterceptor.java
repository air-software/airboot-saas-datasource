package com.airboot.common.security.interceptor;

import com.airboot.common.core.constant.HttpStatus;
import com.airboot.common.core.exception.CustomException;
import com.airboot.common.security.annotation.PreAuthorize;
import com.airboot.common.security.service.AuthService;
import com.airboot.common.security.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 权限拦截器
 *
 * @author airboot
 */
@Slf4j
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    
    @Resource
    private AuthService authService;
    
    @Resource
    private TokenService tokenService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            PreAuthorize annotation = method.getAnnotation(PreAuthorize.class);
            if (annotation != null) {
                String perm = annotation.value();
                if (authService.lacksPermi(perm)) {
                    log.error("---用户没有对应权限, perm={}, token={}---", perm, tokenService.getToken(request));
                    throw new CustomException("没有权限，请联系管理员授权", HttpStatus.FORBIDDEN);
                }
            }
            return true;
        } else {
            return super.preHandle(request, response, handler);
        }
    }
    
}
