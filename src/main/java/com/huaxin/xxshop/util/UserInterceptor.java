package com.huaxin.xxshop.util;

import com.huaxin.xxshop.entity.User;
import com.huaxin.xxshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserInterceptor  implements HandlerInterceptor {
    @Autowired
    private UserService userService = null;
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handle) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user !=null) {
            System.out.println("用户已经登录，为普通会员或管理员");
            return true;
        }else{
        	 response.sendRedirect(request.getContextPath()+"/user/toLogin"); //重定向到登录界面
        	 return false;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
