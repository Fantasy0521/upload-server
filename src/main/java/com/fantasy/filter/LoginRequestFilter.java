package com.fantasy.filter;


import com.fantasy.entity.User;
import com.fantasy.util.StorageUserUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.filter.RequestContextFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录校验请求过滤器
 * OncePerRequestFilter: 保证每次请求只会被验证一次
 */
@Component
public class LoginRequestFilter extends RequestContextFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //跨域请求首先会发送一个OPTIONS确认请求，没问题才会发送真实请求
        if ("OPTIONS".equals(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;//这里如果不return,那么方法会抛异常无法进行真实请求
        }

        String requestURI = request.getRequestURI();
        //需要放行的接口
        if (requestURI.contains("login") || requestURI.contains("register") || requestURI.contains("download") || requestURI.contains("upload/upload")) {
            filterChain.doFilter(request, response);
            return; //这里如果不return,那么SecurityInterceptor响应回来会继续往下执行
        }

        //在请求头中尝试获取currentUserId
        String userId = request.getHeader("currentUserId");
        if (userId == null) {
            throw new RuntimeException("请先登录");
        }

        //验证通过,把用户设置为当前用户
        User currentUser = StorageUserUtil.getUserByUserId(Long.valueOf(userId));
        if (currentUser == null) {
            throw new RuntimeException("请先登录");
        }
        StorageUserUtil.setCurrentUser(currentUser);

        //放行
        filterChain.doFilter(request,response);
    }


}
