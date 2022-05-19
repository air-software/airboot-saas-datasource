package com.airboot.common.security;

/**
 * 登录用户上下文保持
 *
 * @author airboot
 */
public class LoginUserContextHolder {
    
    /**
     * 使用ThreadLocal维护变量，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
     * 所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
     */
    private static final ThreadLocal<LoginUser> CONTEXT_HOLDER = new ThreadLocal<>();
    
    /**
     * 设置登录用户上下文
     */
    public static void setLoginUser(LoginUser loginUser) {
        CONTEXT_HOLDER.set(loginUser);
    }
    
    /**
     * 获取登录用户上下文
     */
    public static LoginUser getLoginUser() {
        return CONTEXT_HOLDER.get();
    }
    
    /**
     * 销毁线程
     */
    public static void destroy() {
        CONTEXT_HOLDER.remove();
    }
}
