package org.hr.sso.client.listener;

import javax.servlet.http.HttpSession;

/**从sso-server验证成功后的回调(用于用户设置自定义数据)
 * @author huangr
 * @version $Id: OnAuthSuccessListener.java, v0.1 2018/9/26 9:14 huangr Exp $$
 */
public interface OnAuthSuccessListener {

    public void setSessionAttribute(HttpSession session);

}
