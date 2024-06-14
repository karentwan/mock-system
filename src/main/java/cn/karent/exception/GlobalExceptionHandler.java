package cn.karent.exception;

import cn.karent.common.Result;
import freemarker.core.InvalidReferenceException;
import freemarker.template.TemplateNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

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
    public Result<Object> invalidReferenceExceptionHandler(InvalidReferenceException e) {
        log.warn("模板渲染不对, 一般是缺少参数: ", e);
        return Result.fail("400", "参数和模板渲染不匹配, 请检查输入参数");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public Result<Object> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.warn("非法状态异常: ", e);
        return Result.fail("400", e.getMessage());
    }

    @ExceptionHandler(RenderException.class)
    @ResponseBody
    public Result<Object> renderExceptionHandler(RenderException e) {
        log.warn("渲染异常: ", e);
        return Result.fail("400", e.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(BindException.class)
    public Result<Object> paramBindExceptionHandler(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        String msg = buildErrorMsg(bindingResult.getAllErrors());
        log.info("invalid request param, msg: {}", msg, e);
        return Result.fail("400", msg);
    }

    private String buildErrorMsg(List<ObjectError> errors) {
        StringBuilder sb = new StringBuilder();
        errors.forEach(error -> {
            if (error instanceof FieldError fieldError) {
                String field = fieldError.getField();
                sb.append(field).append(":");
            }
            sb.append(error.getDefaultMessage()).append(";");
        });
        return sb.toString();
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public Result<Object> throwableExceptionHandler(Throwable e) {
        log.error("系统未知异常: ", e);
        return Result.fail("99", "服务器内部异常");
    }


}
