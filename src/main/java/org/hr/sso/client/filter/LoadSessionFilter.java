package org.hr.sso.client.filter;


import org.hr.sso.client.common.ResponseWrapper;
import org.hr.sso.client.listener.OnSessionLoadedListener;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 统一session获取filter,用于解决分布式并发下多客户端session同时新建不一致的问题
 *
 * @author huangr
 * @version $Id: LoadSessionFilter.java, v0.1 2018/9/25 17:56 huangr Exp $$
 */
public class LoadSessionFilter implements Filter {

    /**
     * session加载后的监听
     */
    private OnSessionLoadedListener sessionLoadedListener;

    /**
     * 是否只有初次创建时监听
     */
    private boolean listenOnCreated = false;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public LoadSessionFilter() {
        this(null);
    }

    public LoadSessionFilter(OnSessionLoadedListener sessionLoadedListener) {
        this(sessionLoadedListener, false);
    }

    public LoadSessionFilter(OnSessionLoadedListener sessionLoadedListener, boolean listenOnCreated) {
        this.sessionLoadedListener = sessionLoadedListener;
        this.listenOnCreated = listenOnCreated;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        boolean created = false;
        HttpSession session = httpRequest.getSession();
        if (session == null) {
            session = httpRequest.getSession(true);
            created = true;
        }
        //根据listenOnCreated参数判断是每次还是第一次触发监听
        if (this.sessionLoadedListener != null && (created || !this.listenOnCreated)) {
            this.sessionLoadedListener.setSessionAttribute(session);
        }
        ResponseWrapper.buildSuccessResponse(httpResponse);
    }

    @Override
    public void destroy() {

    }
}
