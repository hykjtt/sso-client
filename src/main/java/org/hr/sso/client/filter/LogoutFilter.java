package org.hr.sso.client.filter;


import org.hr.sso.client.common.ResponseWrapper;
import org.hr.sso.client.handler.LogoutHandler;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 标准客户端退出操作（需自己实现登出操作，默认登出过滤器请使用DefaultLogoutFilter）
 *
 * @author huangr
 * @version $Id: LogoutFilter.java, v0.1 2018/9/25 17:57 huangr Exp $$
 */
public class LogoutFilter<T> implements Filter {

    private LogoutHandler logoutHandler;

    /**
     * 自定义登出操作需要额外传递的参数，根据实现决定是否需要
     */
    private T param;

    public LogoutFilter(LogoutHandler<T> logoutHandler) {
        this(logoutHandler, null);
    }

    public LogoutFilter(LogoutHandler<T> logoutHandler, T param) {
        this.logoutHandler = logoutHandler;
        this.param = param;
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        this.logoutHandler.executeLogout(request, response, param);

        ResponseWrapper.buildSuccessResponse(response);
    }

    @Override
    public void destroy() {

    }
}
