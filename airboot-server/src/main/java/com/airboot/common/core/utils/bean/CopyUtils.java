package com.airboot.common.core.utils.bean;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体复制工具类
 *
 * @author airboot
 */
public class CopyUtils {
    
    /**
     * 复制单个bean
     */
    public static <T> T copy(Object source, Class<T> target) {
        T result = null;
        try {
            result = target.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        BeanUtils.copyProperties(source, result);
        return result;
    }
    
    /**
     * 复制list
     */
    public static <T> List<T> copyList(List<?> source, Class<T> target) {
        if (source == null || source.size() == 0) {
            return null;
        }
        List<T> resultList = new ArrayList<>();
        for (Object obj : source) {
            resultList.add(copy(obj, target));
        }
        return resultList;
    }
    
    /**
     * 复制mybatis-plus page分页
     */
    public static <T> IPage<T> copyPage(IPage<?> source, Class<T> target) {
        if (source == null) {
            return null;
        }
        IPage<T> resultPage = copy(source, Page.class);
        resultPage.setTotal(source.getTotal());
        resultPage.setRecords(copyList(source.getRecords(), target));
        return resultPage;
    }
    
}
