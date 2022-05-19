package com.airboot.common.model.vo;

import com.airboot.common.core.config.properties.AppProp;
import com.airboot.common.core.constant.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 接口结果包装类
 *
 * @author airboot
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AjaxResult implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 状态码
     */
    private int code;
    
    /**
     * 返回消息
     */
    private String msg;
    
    /**
     * 返回数据
     */
    private Object data;
    
    /**
     * 发生异常时的堆栈数据
     */
    private List<String> stackTraceList;
    
    public AjaxResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
    public AjaxResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    
    public AjaxResult(int code, String msg, Object data, Throwable e) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        if (e != null && AppProp.NOT_PROD_ENV) {
            this.stackTraceList = getStackTraceList(e);
        }
    }
    
    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static AjaxResult success() {
        return AjaxResult.success("操作成功");
    }
    
    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static AjaxResult success(Object data) {
        return AjaxResult.success("操作成功", data);
    }
    
    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static AjaxResult success(String msg, Object data) {
        return new AjaxResult(HttpStatus.SUCCESS, msg, data);
    }
    
    /**
     * 返回错误消息
     *
     * @return
     */
    public static AjaxResult error() {
        return AjaxResult.error("操作失败");
    }
    
    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResult error(String msg) {
        return AjaxResult.error(msg, null);
    }
    
    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResult error(String msg, Throwable e) {
        return AjaxResult.error(msg, null, e);
    }
    
    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static AjaxResult error(String msg, Object data) {
        return new AjaxResult(HttpStatus.ERROR, msg, data);
    }
    
    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static AjaxResult error(String msg, Object data, Throwable e) {
        return new AjaxResult(HttpStatus.ERROR, msg, data, e);
    }
    
    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static AjaxResult error(int code, String msg) {
        return new AjaxResult(code, msg, null);
    }
    
    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param e    异常
     * @return 警告消息
     */
    public static AjaxResult error(int code, String msg, Throwable e) {
        return new AjaxResult(code, msg, null, e);
    }
    
    /**
     * 获取异常的堆栈信息
     * @param e
     * @return
     */
    private static List<String> getStackTraceList(Throwable e) {
        List<String> stackTraceList = new ArrayList<>();
        stackTraceList.add("异常描述：" + e.getClass().toString() + ": " + e.getMessage());
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            String s = "--类:" + stackTraceElement.getFileName() + "--方法:" + stackTraceElement.getMethodName() + "--行数:" + stackTraceElement.getLineNumber();
            stackTraceList.add(s);
        }
        return stackTraceList;
    }
}
