package com.itBoy.common;

import com.itBoy.entity.Common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局的异常处理器
 */

//@ControllerAdvice(annotations = {RestController.class, Controller.class})
//@ResponseBody  // 把最终结果转换为json返回
@Slf4j
public class GlobalExceptionHandler {

    //表示处理这种异常的方法
    //遇到添加的数据，主键已经存在
//    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
//    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception){
//        if(exception.getMessage().contains("Duplicate entry")){
//            String[] s = exception.getMessage().split(" ");
//            String msg =  s[2]+"已经存在";
//            return R.error(msg);
//        }
//        return R.error("未知错误");
//    }
//
//
//    /**
//     * 自定义了一个当遇到的错误为自定义异常
//     * @param exception
//     * @return
//     */
//    @ExceptionHandler(CustomException.class)
//    public R<String> exceptionHandler(CustomException exception){
//        return R.error(exception.getMessage());
//    }
}
