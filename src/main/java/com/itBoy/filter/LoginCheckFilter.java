package com.itBoy.filter;


import com.alibaba.fastjson.JSON;
import com.itBoy.common.BaseContext;
import com.itBoy.entity.Common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javafx.scene.input.KeyCode.R;

/**
 * 检查用户是否已经完成了登录
 * 所有请求都拦截
 */
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //专门用来进行路径匹配的,支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        //1.获取本次请求的uri
        String requestURI = request.getRequestURI();
        //2.判断本次请求是否需要处理
//        下面数组保存了不需要处理的
        String[] urls = new String[]{
                "/**",
                "/employee/login",
                "/employee/login000",
                "/employee/logout"
        };
        boolean check = check(requestURI,urls);
        //3.通过返回值我们已经知道现在需不需要处理
        if(check){
            filterChain.doFilter(request,response);
            return;
        }
        //4-1.判断登录状态,因为我们的登录信息都存放在session内部
        if(request.getSession().getAttribute("employee")!=null){
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request,response);
            return;
        }
        //5.如果没有登录,返回未登录结果,通过输出流的方式向客户端页面相应数据
        response.getWriter().write(JSON.toJSONString(new R(false,"未登录")));
        return;
    }

    /**
     * 下面是一个方法,用来判断是否需要拦截请求
     * 检查本次请求是否需要放行
     */
    public boolean check(String requestURI,String[] urls){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
