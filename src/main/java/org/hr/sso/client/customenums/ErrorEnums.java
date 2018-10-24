package org.hr.sso.client.customenums;

/**
 * @author huangr
 * @version $Id: ErrorEnums.java, v0.1 2018/10/24 9:38 huangr Exp $$
 */
public enum ErrorEnums  implements ErrorInterface{
    SUCCESS(Integer.valueOf(1000), "SUCCESS", "成功"),
    UNKOWN_ERROR(Integer.valueOf(2000), "FAIL", "未知异常");

    private int val;
    private String code;
    private String desc;

    private ErrorEnums(Integer value, String code, String desc) {
        this.val = value.intValue();
        this.code = code;
        this.desc = desc;
    }

    public static ErrorEnums getErrorEnum(int val) {
        ErrorEnums[] var1 = values();
        int var2 = var1.length;
        for(int var3 = 0; var3 < var2; ++var3) {
            ErrorEnums error = var1[var3];
            if(error.getValue() == val) {
                return error;
            }
        }

        throw new IllegalArgumentException("不存在此枚举实例！");
    }

    public String getCode() {
        return null;
    }

    public String getDesc() {
        return null;
    }

    public int getValue() {
        return 0;
    }
}
