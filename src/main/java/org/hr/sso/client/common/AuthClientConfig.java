package org.hr.sso.client.common;

/**
 * @author huangr
 * @version $Id: AuthClientConfig.java, v0.1 2018/10/24 9:32 huangr Exp $$
 */
public class AuthClientConfig {

    /**
     * Http连接池大小
     */
    private Integer httpPoolSize = 10;

    /**
     * Http请求连接超时时间
     */
    private Integer httpTimeOut = 15;

    /**
     * Http请求等待响应超时时间
     */
    private Integer httpReadTimeOut = 15;

    /**
     * 是否使用ssl
     */
    private Boolean useSSL = false;


    /**
     * 无需登录校验的url集合,用“,”分隔
     */
    private String excludeUrls = "";

    /**
     * SSO server服务地址
     */
    private String authServerUrl = "";

    /**
     * 客户端domain 包含协议,如 https://pay.testmail.com
     */
    private String clientDomainUrl = "";

    /**
     * 客户端注销拦截url
     */
    private String logoutFilterUrl = "/api/logout";

    public Integer getHttpPoolSize() {
        return httpPoolSize;
    }

    public void setHttpPoolSize(Integer httpPoolSize) {
        this.httpPoolSize = httpPoolSize;
    }

    public Integer getHttpTimeOut() {
        return httpTimeOut;
    }

    public void setHttpTimeOut(Integer httpTimeOut) {
        this.httpTimeOut = httpTimeOut;
    }

    public Integer getHttpReadTimeOut() {
        return httpReadTimeOut;
    }

    public void setHttpReadTimeOut(Integer httpReadTimeOut) {
        this.httpReadTimeOut = httpReadTimeOut;
    }

    public Boolean getUseSSL() {
        return useSSL;
    }

    public void setUseSSL(Boolean useSSL) {
        this.useSSL = useSSL;
    }


    public String getExcludeUrls() {
        return excludeUrls;
    }

    public void setExcludeUrls(String excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

    public String getAuthServerUrl() {
        return authServerUrl;
    }

    public void setAuthServerUrl(String authServerUrl) {
        this.authServerUrl = authServerUrl;
    }

    public String getClientDomainUrl() {
        return clientDomainUrl;
    }

    public void setClientDomainUrl(String clientDomainUrl) {
        this.clientDomainUrl = clientDomainUrl;
    }

    public String getLogoutFilterUrl() {
        return logoutFilterUrl;
    }

    public void setLogoutFilterUrl(String logoutFilterUrl) {
        this.logoutFilterUrl = logoutFilterUrl;
    }
}
