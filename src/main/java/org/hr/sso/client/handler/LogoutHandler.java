package org.hr.sso.client.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义登出操作
 *
 * @author huangr
 * @version $Id: LogoutHandler.java, v0.1 2018/9/27 14:32 huangr Exp $$
 */
public interface LogoutHandler<T> {

    public void executeLogout(HttpServletRequest request, HttpServletResponse response, T param);

}