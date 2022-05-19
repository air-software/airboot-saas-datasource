package com.airboot.common.core.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Long型序列化配置
 *
 * @author airboot
 */

public class JsonLongSerializer extends JsonSerializer<Long> {
    
    public static final JsonLongSerializer INSTANCE = new JsonLongSerializer();
    
    public JsonLongSerializer() {}
    
    @Override
    public void serialize(Long value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (value != null) {
            // 如果大于JS整数最大值2^53或小于最小值-2^53则转成String，防止精度丢失
            if (value > (Math.pow(2, 53) - 1) || value < (1 - Math.pow(2, 53))) {
                jsonGenerator.writeString(String.valueOf(value));
            } else {
                jsonGenerator.writeNumber(value);
            }
        } else {
            jsonGenerator.writeNull();
        }
    }
    
}
