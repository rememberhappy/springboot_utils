package com.springbootutils.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JacksonUtil,快速方便对象和json字符串互相转换
 *
 * @author dingxs
 */
public class JacksonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {

        OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        OBJECT_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        OBJECT_MAPPER.disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        OBJECT_MAPPER.disable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        OBJECT_MAPPER.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        // 支持单引号
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }


    /**
     * 私有化构造器
     */
    private JacksonUtil() {
    }

    /**
     * json字符串转对象
     *
     * @param json          json字符串
     * @param typeReference 带泛型的类型
     * @param objectMapper  objectMapper
     * @param <T>           类型
     * @return 对象
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference, ObjectMapper objectMapper) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            return objectMapper == null
                    ? OBJECT_MAPPER.readValue(json, typeReference)
                    : objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            logger.info(e.getMessage(), e);
            return null;
        }
    }

    /**
     * json字符串转对象
     *
     * @param json          json字符串
     * @param typeReference 带泛型的类型
     * @param <T>           类型
     * @return 对象
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        return fromJson(json, typeReference, null);
    }

    /**
     * json字符串转对象
     *
     * @param json         json字符串
     * @param clazz        对象类对象
     * @param objectMapper objectMapper
     * @param <T>          泛型
     * @return 对象
     */
    public static <T> T fromJson(String json, Class<T> clazz, ObjectMapper objectMapper) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            return objectMapper == null
                    ? OBJECT_MAPPER.readValue(json, clazz)
                    : objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * json字符串转对象
     *
     * @param json  json字符串
     * @param clazz 对象类对象
     * @param <T>   泛型
     * @return 对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return fromJson(json, clazz, null);
    }

    /**
     * 对象转字符串
     *
     * @param t            对象
     * @param objectMapper objectMapper
     * @param <T>          泛型
     * @return json字符串
     */
    public static <T> String toJson(T t, ObjectMapper objectMapper) {
        if (t == null) {
            return null;
        }
        try {
            return objectMapper == null
                    ? OBJECT_MAPPER.writeValueAsString(t)
                    : objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            logger.info(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 对象转json字符串
     *
     * @param t   对象
     * @param <T> 对象类型
     * @return json字符串
     */
    public static <T> String toJson(T t) {
        return toJson(t, null);
    }


}
