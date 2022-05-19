package com.airboot.common.core.exception.user;

/**
 * 用户不存在异常类
 *
 * @author airboot
 */
public class UserNotExistException extends UserException {
    
    private static final long serialVersionUID = 1L;
    
    public UserNotExistException() {
        super("user.not.exist", null);
    }
}
