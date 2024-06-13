package cn.karent.exception;

import cn.karent.common.Result;
import freemarker.core.InvalidReferenceException;
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
        log.warn("未找到模板: ", e);
        return Result.fail("404", "接口不存在");
    }

    @ExceptionHandler(InvalidReferenceException.class)
    @ResponseBody
    public Result<Object> invalidReferenceExceptionHandler(TemplateNotFoundException e) {
        log.warn("模板渲染不对, 一般是缺少参数: ", e);
        return Result.fail("400", "参数和模板渲染不匹配, 请检查");
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public Result<Object> throwableExceptionHandler(Throwable e) {
        log.error("系统未知异常: ", e);
        return Result.fail("99", "服务器内部异常");
    }


}
