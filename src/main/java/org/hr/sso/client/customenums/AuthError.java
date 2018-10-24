package org.hr.sso.client.customenums;

/**
 * @author huangr
 * @version $Id: AuthError.java, v0.1 2018/10/24 9:47 huangr Exp $$
 */
public  enum AuthError implements ErrorInterface {

    USERNAME_OR_PWD_ERROR(5200, "USERNAME_OR_PWD_ERROR", "用户名或密码错误"),
    NEED_LOGIN(Integer.valueOf(5201).intValue(), "NEED_LOGIN", "请校验Token"),
    REDIRECT_TO_LOGIN_PAGE(Integer.valueOf(5202).intValue(), "REDIRECT_TO_LOGIN_PAGE", "跳转到登录页面"),
    NOT_LOGIN(Integer.valueOf(5203).intValue(), "NOT_LOGIN", "未登录"),
    TIME_OUT(Integer.valueOf(5204).intValue(), "TIME_OUT", "登录超时"),
    INVALID_TICKET(Integer.valueOf(5205).intValue(), "INVALID_TICKET", "无效的Ticket"),
    TICKET_IS_NULL(Integer.valueOf(5206).intValue(), "TICKET_IS_NULL", "未获取到Ticket"),
    NETWORK_ERROR(Integer.valueOf(5207).intValue(), "NETWORK_ERROR", "网络异常"),
    REMOVE_SESSION_FAILURE(Integer.valueOf(5208).intValue(), "REMOVE_SESSION_FAILURE", "移除session失败");

    private int val;
    private String code;
    private String desc;

    private AuthError(int val, String code, String desc) {
        this.val = val;
        this.code = code;
        this.desc = desc;
    }

    public static AuthError getErrorEnum(int val) {
        AuthError[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            AuthError error = var1[var3];
            if(error.getValue() == val) {
                return error;
            }
        }

        return null;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public int getValue() {
        return this.val;
    }

}
