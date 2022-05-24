package com.airboot.common.core.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.airboot.common.core.constant.Constants;
import com.airboot.common.core.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 枚举工具类
 *
 * @author airboot
 */
@Slf4j
@Component
public class EnumUtil {
    
    private static final String ENUM_PACKAGE = "com.airboot.**.model.enums";
    
    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    
    private static Map<String, String> ENUM_MAP = new HashMap<>();
    
    @PostConstruct
    private void init() {
        // 将包路径转换为文件目录路径
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
            + ClassUtils.convertClassNameToResourcePath(new StandardEnvironment().resolveRequiredPlaceholders(ENUM_PACKAGE))
            + '/'
            + DEFAULT_RESOURCE_PATTERN;
        ResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
        try {
            // 通过Spring的resourceloader直接获取路径下的class文件
            Resource[] resources = resourceLoader.getResources(packageSearchPath);
            for (Resource resource : resources) {
                String filePath = resource.getURL().getPath();
                String enumFullName = "com." + filePath.replaceAll("/", ".").split("com\\.")[1].replaceAll(".class", "");
    
                String[] strArr = StringUtils.split(enumFullName, ".");
                ENUM_MAP.put(strArr[strArr.length - 1], enumFullName);
            }
        } catch (Exception e) {
            log.error("---初始化EnumUtil异常---", e);
            e.printStackTrace();
        }
    }
    
    /**
     * 获取系统中的所有枚举
     *
     * @return 包含系统所有枚举的Map，key: 枚举简单名，value: 枚举全限定名
     */
    public static Map<String, String> getAllEnums() {
        return ENUM_MAP;
    }
    
    /**
     * 根据表字段名获取对应枚举类全限定名
     *
     * @param columnName 字段名
     * @return 枚举类全限定名
     */
    public static String getEnumFullNameByTableColumn(String columnName) {
        String key = StringUtils.snakeToPascalCase(columnName) + "Enum";
        return ENUM_MAP.get(key);
    }
    
    /**
     * 获得枚举名对应指定字段值的有序Map<br>
     * 键为枚举名，值为字段值
     *
     * @param clazz     枚举类
     * @param fieldName 字段名，最终调用getXXX方法
     * @param reverse   是否反转
     * @return 枚举名对应指定字段值的Map
     */
    public static Map<Object, Object> getEnumMap(Class<? extends Enum<?>> clazz, String fieldName, boolean reverse) {
        final Enum<?>[] enums = clazz.getEnumConstants();
        if (null == enums) {
            return null;
        }
        final Map<Object, Object> map = MapUtil.newHashMap(enums.length, true);
        for (Enum<?> e : enums) {
            if (reverse) {
                map.put(ReflectUtil.getFieldValue(e, fieldName), e.name());
            } else {
                map.put(e.name(), ReflectUtil.getFieldValue(e, fieldName));
            }
        }
        return map;
    }
    
    public static Map<Object, Object> getNameFieldMap(Class<? extends Enum<?>> clazz, String fieldName) {
        return getEnumMap(clazz, fieldName, false);
    }
    
    public static Map<Object, Object> getFieldNameMap(Class<? extends Enum<?>> clazz, String fieldName) {
        return getEnumMap(clazz, fieldName, true);
    }
    
    /**
     * 获得枚举名对应Code的Map
     * 键为枚举名，值为Code
     */
    public static Map<Object, Object> getNameCodeMap(Class<? extends Enum<?>> clazz) {
        return getNameFieldMap(clazz, "code");
    }
    
    /**
     * 获得Code对应枚举名的Map
     * 键为Code，值为枚举名
     */
    public static Map<Object, Object> getCodeNameMap(Class<? extends Enum<?>> clazz) {
        return getFieldNameMap(clazz, "code");
    }
    
    /**
     * 获取枚举的name列表
     *
     * @param clazz 枚举类
     * @return name列表
     */
    public static List<String> getNameList(Class<?> clazz) {
        List<String> nameList = new ArrayList<>();
        Object[] enumConstants = clazz.getEnumConstants();
        if (enumConstants != null) {
            for (Object enumConstant : enumConstants) {
                Enum enumobj = (Enum) enumConstant;
                nameList.add(enumobj.name());
            }
        }
        return nameList;
    }
    
    /**
     * 获取系统中的所有枚举name列表
     *
     * @return 包含系统所有枚举name列表的Map，key: 枚举简单名（驼峰式并去掉Enum后缀），value: 枚举name列表
     */
    public static Map<String, List<String>> getAllNameList() {
        Map<String, List<String>> resultMap = new HashMap<>();
        ENUM_MAP.forEach((simpleName, fullName) -> {
            try {
                Class<?> enumClass = Class.forName(fullName);
                String key = StrUtil.removeSufAndLowerFirst(simpleName, "Enum");
                resultMap.put(key, getNameList(enumClass));
            } catch (Exception e) {
                log.error("---获取枚举类异常, 枚举类全限定名={}---", fullName, e);
                throw new CustomException("获取枚举类异常", e);
            }
        });
        return resultMap;
    }
    
    /**
     * 获取系统中所有可展示给前端的枚举name列表
     *
     * @return 包含系统所有所有可展示给前端的枚举name列表的Map，key: 枚举简单名（驼峰式并去掉Enum后缀），value: 枚举name列表
     */
    public static Map<String, List<String>> getAllFrontShowNameList() {
        Map<String, List<String>> resultMap = new HashMap<>();
        ENUM_MAP.forEach((simpleName, fullName) -> {
            try {
                Class<?> enumClass = Class.forName(fullName);
                String key = StrUtil.removeSufAndLowerFirst(simpleName, "Enum");
                if (StringUtils.equalsAnyIgnoreCase(key, Constants.FRONT_SHOW_ENUMS)) {
                    resultMap.put(key, getNameList(enumClass));
                }
            } catch (Exception e) {
                log.error("---获取枚举类异常, 枚举类全限定名={}---", fullName, e);
                throw new CustomException("获取枚举类异常", e);
            }
        });
        return resultMap;
    }
    
}
