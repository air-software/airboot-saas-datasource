package com.airboot.common.component;

import com.airboot.common.core.utils.DateUtils;
import com.airboot.common.model.vo.AjaxResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * web层通用数据处理
 *
 * @author airboot
 */
public class BaseController {
    
    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }
    
    /**
     * 返回成功结果
     */
    protected AjaxResult success() {
        return AjaxResult.success();
    }
    
    protected AjaxResult success(Object data) {
        return AjaxResult.success(data);
    }
    
    /**
     * 返回失败结果
     */
    protected AjaxResult fail() {
        return AjaxResult.error();
    }
    
    protected AjaxResult fail(String msg) {
        return AjaxResult.error(msg);
    }
    
    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }
    
    /**
     * 响应返回结果
     *
     * @param success 是否成功
     * @return 操作结果
     */
    protected AjaxResult toAjax(boolean success) {
        return success ? AjaxResult.success() : AjaxResult.error();
    }
}
