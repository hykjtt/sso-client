package org.hr.sso.client.customenums;

/**
 * @author huangr
 * @version $Id: ErrorInterface.java, v0.1 2018/10/24 9:37 huangr Exp $$
 */
public interface ErrorInterface {
    String getCode();

    String getDesc();

    int getValue();
}
