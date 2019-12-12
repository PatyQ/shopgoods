package com.cy.Controller;

import com.cy.entity.ResultData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

//需要添加spring-boot-starter
@ControllerAdvice
@ResponseBody
public class ExceptionControllerAdvice {

    @ExceptionHandler(Exception.class)
    public Object exceptionHandler(HttpServletRequest request){

        //获取请求头的-数据
        String header = request.getHeader("X-Requested-With");

        //如果请求头是这个则证明发送的是ajax请求
        if (null != header && "XMLHttpRequest".equals(header)){

            return new ResultData<>().setCode(ResultData.ResultCodeList.ERROR).setMsg("恭喜你获得错误的页面!");
        }else {
            //modelandview会强制返回视图,无视responsebody
            return new ModelAndView("myerror");
        }
    }
}
