package org.ks365.osmp.common.exception;

import org.apache.shiro.authz.UnauthorizedException;
import org.ks365.osmp.common.entity.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 异常统一处理
 *
 * @author tianslic
 */
@RestControllerAdvice
public class GlobalExceptionAdvice {
    private static final int SESSION_EXPIRE = 401;
    private static final int UNAUTHORIZED = 302;

    Logger logger = LoggerFactory.getLogger(Exception.class);

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity ConstraintViolationExceptionHandler(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(","));
        logger.warn(message);
        return new ResponseEntity().faild(message);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity paramValidate(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        logger.warn(message);
        return new ResponseEntity().faild(message);
    }

    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity Unauthorized(UnauthorizedException e, HttpServletResponse response) {
        logger.warn("暂无调用权限 {}", e.getMessage());
        ResponseEntity responseEntity = new ResponseEntity();
        response.setStatus(UNAUTHORIZED);
        return responseEntity.faild("暂无调用权限" + e.getMessage().replace("Subject does not have permission", ""));
    }

    @ExceptionHandler(SessionExpireException.class)
    public void loginTimeout(HttpServletResponse response) {
        logger.warn("session失效");
        response.setStatus(SESSION_EXPIRE);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity error(Exception e, HttpServletResponse response) {
        logger.error("请求异常");
        e.printStackTrace();
        response.setStatus(500);
        return new ResponseEntity().faild("请求异常");
    }

}
