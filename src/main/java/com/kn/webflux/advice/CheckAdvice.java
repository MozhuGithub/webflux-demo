package com.kn.webflux.advice;

import com.kn.webflux.exception.CheckException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

/**
 * @ClassName: CheckAdvice
 * @Description TODO: 参数校验异常处理切面
 * @Date: 2019/11/4 18:46
 * @Author: Kn
 */
@ControllerAdvice
public class CheckAdvice {
    /**
     * @Description TODO: 捕获参数校验异常
     * @Param: [exception]
     * @Return: ResponseEntity<String>
     * @Author: Kn
     * @Date: 2019/11/4 23:00
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<String> handleBindException(WebExchangeBindException exception) {
        return new ResponseEntity<String>(toStr(exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CheckException.class)
    public ResponseEntity<String> handleCheckException(CheckException exception) {
        return new ResponseEntity<String>(toStr(exception), HttpStatus.BAD_REQUEST);
    }

    private String toStr(CheckException exception) {
        return exception.getFieldName() + ":错误的值" + exception.getFieldValue();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException runtimeException) {
        return new ResponseEntity<>(runtimeException.getClass().getSimpleName(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * @Description TODO: 将校验异常转换为字符串
     * @Param: [exception]
     * @Return: java.lang.String
     * @Author: Kn
     * @Date: 2019/11/4 18:55
     */
    private String toStr(WebExchangeBindException exception) {
        return exception.getFieldErrors()
                .stream()
                //getField() 返回对象受影响的字段
                .map(e -> e.getField() + ":" + e.getDefaultMessage())
                .reduce("", (s1, s2) -> s1 + "\n" + s2);
    }
}
