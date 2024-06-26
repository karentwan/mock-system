package cn.karent.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 响应包装
 *
 * @author wanshengdao
 * @date 2024/6/13
 */
@Slf4j
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Result<T> {

    private String retCode;

    private String retMsg;

    private T data;

    public static <T> Result<T> ok(T t) {
        Result<T> result = new Result<>();
        result.setRetCode("00");
        result.setRetMsg("success");
        result.setData(t);
        return result;
    }

    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static Result<Object> fail(String retCode, String retMsg) {
        Result<Object> result = new Result<>();
        result.setRetCode(retCode);
        result.setRetMsg(retMsg);
        return result;
    }

}
