package cn.karent.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author wanshengdao
 * @date 2024/6/13
 */
public abstract class JsonUtils {

    public static ObjectMapper getMapper() {
        return ObjectMapperHolder.INSTANCE;
    }

    public static String toString(Object obj) {
        try {
            return getMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] toBytes(Object obj) {
        try {
            return getMapper().writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObject(String jsonStr, Class<T> type) {
        try {
            return getMapper().readValue(jsonStr, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObject(String jsonStr, TypeReference<T> typeReference) {
        try {
            return getMapper().readValue(jsonStr, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> parseList(String jsonStr, Class<T> type) {
        JavaType javaType = getMapper().getTypeFactory().constructCollectionType(List.class, type);
        try {
            return getMapper().readValue(jsonStr, javaType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> parseMap(String jsonStr) {
        MapType mapType = getMapper().getTypeFactory().constructMapType(Map.class, String.class, Object.class);
        try {
            return getMapper().readValue(jsonStr, mapType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> toMap(Object src) {
        MapType mapType = getMapper().getTypeFactory().constructMapType(Map.class, String.class, Object.class);
        return getMapper().convertValue(src, mapType);
    }

    public static <T> T fromMap(Map<String, ?> map, Class<T> type) {
        return getMapper().convertValue(map, type);
    }

    private static class ObjectMapperHolder {

        private static final ObjectMapper INSTANCE = createObjectMapper();

        private static ObjectMapper createObjectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
            mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true);
            // 允许双斜杠注释
            mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.setTimeZone(TimeZone.getTimeZone("GMT"));
            // 时间支持
            mapper.registerModule(new JavaTimeModule());
            return mapper;
        }
    }
}
