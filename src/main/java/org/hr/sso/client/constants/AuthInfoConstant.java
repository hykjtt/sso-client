package org.hr.sso.client.constants;

/** 请求字段常量
 * @author huangr
 * @version $Id: AuthInfoConstant.java, v0.1 2018/10/21 13:38 huangr Exp $$
 */
public class AuthInfoConstant {
    /**
     * 票据在URL中的属性名
     */
    public static final String  TICKET_ATTR_NAME     = "ticket";

    /**
     * 登出地址在URL中的属性名
     */
    public static final String  LOGOUT_ATTR_NAME     = "logoutUrl";

    /**
     * session在URL中的属性名
     */
    public static final String  SESSION_ATTR_NAME     = "sessionId";

    /**
     * 用户数据存在Session中的key
     */
    public static final String  LOGIN_ATTR_NAME      = "user";

    /**
     * 判断session是否已经存在的key
     */
    public static final String  SESSION_FLAG_ATTR      = "sessionCreated";

    /**
     * userInfo在临时map中的key
     */
    public static final String  USER_INFO_ATTR      = "userInfo";

    /**
     * 验证票据的URL
     */
    public static final String  VALIDATE_TICKET_PATH  = "/api/v1.0/auth/ticket/verify";

    /**
     * 删除验证完毕的ticket
     */
    public static final String  INVALIDATE_TICKET = "/api/v1.0/auth/ticket/invalidate";


    /**
     * 响应数据编码
     */
    public static final   String RESPONSE_ENCODING = "UTF-8";

    /**
     *  响应数据类型
     */
    public  static  final String CONTENT_TYPE = "application/json; charset=utf-8";

}
