package org.hr.sso.client.common;

import com.alibaba.fastjson.JSONObject;
import org.hr.sso.client.customenums.AuthError;
import org.hr.sso.client.customenums.ErrorEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import  static  org.hr.sso.client.constants.AuthInfoConstant.*;

/**
 * @author huangr
 * @version $Id: ResponseWrapper.java, v0.1 2018/10/24 10:09 huangr Exp $$
 */
public class ResponseWrapper {
    private static Logger logger = LoggerFactory.getLogger(ResponseWrapper.class);

    /**
     * 限制构造器访问
     */
    private ResponseWrapper(){

    }

    public static void buildNeedLoginResponse(HttpServletResponse response) {
        StResult result = new StResult(false, AuthError.NEED_LOGIN);
        buildResponse(response, result);
    }

    public static void buildSuccessResponse(HttpServletResponse response) {
        StResult result = new StResult(true, ErrorEnums.SUCCESS);
        buildResponse(response, result);
    }


    public static void buildNetworkException(HttpServletResponse response) {
        StResult result = new StResult(false, AuthError.NETWORK_ERROR);
        buildResponse(response, result);
    }

    public static void buildSessionRemoveException(HttpServletResponse response) {
        StResult result = new StResult(false, AuthError.REMOVE_SESSION_FAILURE);
        buildResponse(response, result);
    }

    private static void buildResponse(HttpServletResponse response, StResult result) {
        response.setCharacterEncoding(RESPONSE_ENCODING);
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(JSONObject.toJSONString(result));
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
