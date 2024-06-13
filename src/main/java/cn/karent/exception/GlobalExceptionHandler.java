package cn.karent.exception;

import cn.karent.common.Result;
import freemarker.template.TemplateNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 *
 * @author wanshengdao
 * @date 2024/6/13
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TemplateNotFoundException.class)
    @ResponseBody
    public Result<Object> templateNotFoundHandler(TemplateNotFoundException e) {
        log.error("模板异常: ", e);
        return Result.fail("404", "接口不存在");
    }


    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public Result<Object> throwableExceptionHandler(Throwable e) {
        log.error("系统未知异常: ", e);
        return Result.fail("99", "服务器内部异常");
    }


}
