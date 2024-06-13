package cn.karent.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import java.util.List;
import java.util.Map;

/**
 * @author wanshengdao
 * @date 2024/6/13
 */
public class JsonUtils {

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
        private static final ObjectMapper INSTANCE = ObjectMapperFactory.createConfiguredMapper();
    }
}
