package com.ms.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ms.entity.system.User;
import com.ms.util.Const;

/**
 * 
 * 类名称：LoginHandlerInterceptor.java 类描述：
 * 
 * @author FH 创建时间�?2016�?8�?15�?
 * @version 1.6
 */
public class LoginHandlerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        // TODO Auto-generated method stub
        String path = request.getServletPath();
        if (path.matches(Const.NO_INTERCEPTOR_PATH)) {
            return true;
        } else {
            // shiro管理的session
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();
            User user = (User) session.getAttribute(Const.SESSION_USER);
            if (user != null) {
                path = path.substring(1, path.length());
                return true;
            } else {
                // 登陆过滤
                response.sendRedirect(request.getContextPath() + Const.LOGIN);
                return false;
                // return true;
            }
        }
    }

}
