package com.huaxin.xxshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserInfoAction {
    /**
     * 用户注册信息校验
     */
    @RequestMapping("/user_isexist")
    public String user_isexist(){
        return "asda";
    }

}
