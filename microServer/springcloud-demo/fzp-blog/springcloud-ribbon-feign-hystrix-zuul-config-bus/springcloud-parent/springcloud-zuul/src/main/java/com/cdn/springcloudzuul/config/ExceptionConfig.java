package com.cdn.springcloudzuul.config;

import com.thoughtworks.xstream.core.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public String baseErrorHandler(HttpServletRequest request, Exception e) {
        return "redirect:http://www.baidu.com";
    }

}