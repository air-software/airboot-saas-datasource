package com.airboot.common.core.config;

import com.airboot.common.core.config.properties.AppProp;
import com.airboot.common.core.constant.Constants;
import com.airboot.common.core.interceptor.RepeatSubmitInterceptor;
import com.airboot.common.security.interceptor.AuthInterceptor;
import com.airboot.common.security.interceptor.LoginInterceptor;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * MVC配置
 *
 * @author airboot
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Resource
    private LoginInterceptor loginInterceptor;
    
    @Resource
    private AuthInterceptor authInterceptor;
    
    @Resource
    private RepeatSubmitInterceptor repeatSubmitInterceptor;
    
    @Resource
    private HttpMessageConverters httpMessageConverters;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 本地文件上传路径
        registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**").addResourceLocations("file:" + ProjectConfig.getProfile() + "/");
        
        // swagger配置
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
    
    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
            .excludePathPatterns(Constants.EXCLUDE_PATH_PATTERNS);
        registry.addInterceptor(authInterceptor).addPathPatterns("/**")
            .excludePathPatterns(Constants.EXCLUDE_PATH_PATTERNS);
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**")
            .excludePathPatterns(Constants.EXCLUDE_PATH_PATTERNS);
    }
    
    /**
     * MappingJackson2HttpMessageConverter 实现了HttpMessageConverter 接口，
     * httpMessageConverters.getConverters() 返回的对象里包含了MappingJackson2HttpMessageConverter
     */
    @Bean
    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter(new JacksonMapper());
    }
    
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.addAll(httpMessageConverters.getConverters());
    }
    
    /**
     * 跨域请求配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 只有非生产环境才会设置允许跨域
        if (AppProp.NOT_PROD_ENV) {
            registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("*")
                .allowedHeaders("*");
        }
    }
    
}
