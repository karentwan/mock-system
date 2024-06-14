package cn.karent.exception;

/**
 * 渲染异常
 *
 * @author wanshengdao
 * @date 2024/6/14
 */
public class RenderException extends RuntimeException{

    public RenderException(Throwable cause) {
        super(cause);
    }

    public RenderException(String message, Throwable cause) {
        super(message, cause);
    }
}
