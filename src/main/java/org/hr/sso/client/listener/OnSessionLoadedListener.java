package org.hr.sso.client.listener;

import javax.servlet.http.HttpSession;

/** 统一session加载后触发监听
 * @author huangr
 * @version $Id: OnSessionLoadedListener.java, v0.1 2018/9/27 13:52 huangr Exp $$
 */
public interface OnSessionLoadedListener {

    public void setSessionAttribute(HttpSession session);
}
