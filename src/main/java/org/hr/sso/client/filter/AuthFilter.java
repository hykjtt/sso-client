package org.hr.sso.client.filter;

import com.alibaba.fastjson.JSONObject;
import org.hr.sso.client.common.AuthClientConfig;
import org.hr.sso.client.common.ResponseWrapper;
import org.hr.sso.client.customenums.AuthError;
import org.hr.sso.client.listener.OnAuthSuccessListener;
import org.hr.sso.client.retryer.RetryerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hr.sso.client.constants.AuthInfoConstant.*;


/**
 * 认证过滤器
 *
 * @author huangr
 * @version $Id: AuthFilter.java, v0.1 2018/9/21 14:14 huangr Exp $$
 */
public class AuthFilter implements Filter {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class);

    private RestTemplate restTemplate = new RestTemplate();

    private OnAuthSuccessListener authSuccessListener;
    /**
     * 参数配置
     */
    private AuthClientConfig authClientConfig;

    /**
     * 创建并配置认证filter
     *
     * @param authClientConfig
     */
    public AuthFilter(AuthClientConfig authClientConfig) {
        this(authClientConfig, null);
    }

    /**
     * 创建并配置认证filter
     * 携带自定义监听事件
     *
     * @param authClientConfig
     */
    public AuthFilter(AuthClientConfig authClientConfig, OnAuthSuccessListener authSuccessListener) {
        if (authSuccessListener != null) {
            this.authSuccessListener = authSuccessListener;
        }
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String servletPath = httpRequest.getServletPath();

        //无需登录校验的url,不再向sso-server发起认证
        if (isExcludedUrl(servletPath)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        HttpSession session = httpRequest.getSession(true);
        //session为已登录状态，无需验证
        if (session.getAttribute(LOGIN_ATTR_NAME) != null) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            String ticket = httpRequest.getParameter(TICKET_ATTR_NAME);
            //无session有ticket,则向sso-server发起ticket验证并获取登录状态
            if (ticket != null) {
                Map<String, Object> resultInfo = this.doRetryAuth(session, ticket);
                //并发情况下，其他客户端已经设置了session
                if (Boolean.valueOf(resultInfo.get(SESSION_FLAG_ATTR).toString())) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {//验证ticket获取到的用户信息
                    Object userInfo = resultInfo.get(USER_INFO_ATTR);
                    if (null == userInfo) { //表示重试之后也没有获取到用户数据
                        ResponseWrapper.buildNetworkException(httpResponse);
                        return;
                    }
                    //ticket验证通过，设置session为已登录状态
                    if (StringUtils.hasText(userInfo.toString())) {
                        session.setAttribute(LOGIN_ATTR_NAME, userInfo);
                        //删除ticket
                        try {
                            this.doInvalidateTicket(ticket);
                        } catch (RuntimeException e) {
                            LOGGER.error(e.getMessage());
                        }
                        //触发验证成功监听
                        if (this.authSuccessListener != null) {
                            this.authSuccessListener.setSessionAttribute(session);
                        }
                        filterChain.doFilter(servletRequest, servletResponse);
                    } else {
                        ResponseWrapper.buildNeedLoginResponse(httpResponse);
                    }
                }
            } else {
                //无session且无ticket,则跳转到login接口登录或者从token中获取ticket
                ResponseWrapper.buildNeedLoginResponse(httpResponse);
            }
        }
    }

    @Override
    public void destroy() {

    }

    /**
     * 移除认证完毕的ticket
     *
     * @param ticket
     * @throws RuntimeException
     */
    private void doInvalidateTicket(String ticket) throws RuntimeException {
        Map<String,Object> verifyParams = new HashMap();
        verifyParams.put(TICKET_ATTR_NAME, ticket);
        JSONObject jsonObject;
        try {
              jsonObject  = restTemplate.getForObject(authClientConfig.getAuthServerUrl() + INVALIDATE_TICKET,JSONObject.class,verifyParams);
        } catch(Exception e) {
            throw new RuntimeException("与sso-server通信异常，删除ticket失败");
        }
        boolean result = Boolean.valueOf(jsonObject.get("success").toString());
        if (!result) {
            throw new RuntimeException("ticket删除失败");
        }
    }

    /**
     * 使用重试框架进行ticket验证
     *
     * @param session
     * @param ticket
     * @return
     */
    private Map<String, Object> doRetryAuth(final HttpSession session, String ticket) {
        JSONObject verifyParams = new JSONObject();
        verifyParams.put(TICKET_ATTR_NAME, ticket);
        verifyParams.put(LOGOUT_ATTR_NAME, authClientConfig.getClientDomainUrl() + authClientConfig.getLogoutFilterUrl());
        verifyParams.put(SESSION_ATTR_NAME, session.getId());
        //使用okHttpClient发起ticket认证
        final Map<String, Object> resultMap = new HashMap<>();
        try {
            RetryerHelper.execute(new RetryCallback<Void, Throwable>() {
                @Override
                public Void doWithRetry(RetryContext retryContext) throws Throwable {
                    //每次ticket验证失败后获取session——验证失败意味着其他客户端验证成功并设置了session
                    if (session.getAttribute(LOGIN_ATTR_NAME) != null) {
                        resultMap.put(SESSION_FLAG_ATTR, true);
                    } else {
                        //HTTPResponseParser,保证操作在响应成功之后;
                        JSONObject jsonObject = restTemplate.postForObject(authClientConfig.getAuthServerUrl() + VALIDATE_TICKET_PATH,verifyParams,JSONObject.class);
                        boolean success = Boolean.valueOf(jsonObject.get("success").toString());
                        if (!success) {
                            //若失败，则重试
                            throw new RuntimeException(AuthError.INVALID_TICKET.getDesc());
                        } else {
                            //验证成功，表示需要设置session
                            resultMap.put(SESSION_FLAG_ATTR, false);
                            resultMap.put(USER_INFO_ATTR, jsonObject.get("data"));
                        }
                    }
                    return null;
                }
            });
        } catch (Throwable throwable) {
            LOGGER.error(AuthError.INVALID_TICKET.getDesc(), throwable);
            resultMap.put(SESSION_FLAG_ATTR, false);
        }
        return resultMap;
    }

    /**
     * 判断当前请求是否处于认证例外的url
     *
     * @param servletPath
     * @return
     */
    private boolean isExcludedUrl(String servletPath) {
        String excludeUrls = authClientConfig.getExcludeUrls();
        boolean result = false;
        //空表示没有例外
        if (StringUtils.isEmpty(excludeUrls)) {
            return result;
        }
        String[] urlArray = excludeUrls.split(",");
        if (urlArray.length < 1) {
            return result;
        }
        for (String excludeUrl : urlArray) {
            //表示通配路径匹配，覆盖所有子路径
            if (excludeUrl.endsWith("/*")) {
                if (servletPath.startsWith(excludeUrl.replace("/*", ""))) {
                    result = true;
                }
            } else {
                //精确匹配
                if (excludeUrl.equals(servletPath)) {
                    result = true;
                }

            }
        }
        return result;
    }
}
