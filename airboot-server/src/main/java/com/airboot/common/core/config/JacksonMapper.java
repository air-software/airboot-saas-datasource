package com.airboot.common.core.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * jackson序列化配置
 *
 * @author airboot
 */
public class JacksonMapper extends ObjectMapper {
    
    public JacksonMapper() {
        super();
        this.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        this.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, JsonLongSerializer.INSTANCE);
        simpleModule.addSerializer(Long.TYPE, JsonLongSerializer.INSTANCE);
        simpleModule.addSerializer(long.class, JsonLongSerializer.INSTANCE);
        registerModule(simpleModule);
    }
}

