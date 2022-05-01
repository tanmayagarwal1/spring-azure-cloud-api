package com.spring.azure.springazurecloud.handlers;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.dto.response.ResponseDto;
import com.spring.azure.springazurecloud.exception.general.JsonConversionException;
import com.spring.azure.springazurecloud.utils.JsonUtil;
import com.spring.azure.springazurecloud.utils.LogHelper;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class ResponseHandler {
    public static void handleResponse(HttpServletResponse response,
                                      HttpStatus status,
                                      ResponseDto responseDto){

        response.setContentType("application/json");
        String json = JsonUtil.toJson(responseDto).orElse(Constants.PLACEHOLDER);
        if (json.isEmpty()) throw new JsonConversionException("Error in Converting to Json");

        response.setStatus(status.value());
        try {
            PrintWriter printWriter =  response.getWriter();
            printWriter.println(json);
            printWriter.close();
        }
        catch (Exception exception){
            LogHelper.logError("Exception ", exception);
        }

    }

    public static <T> void handleResponse(HttpServletResponse response,
                                      HttpStatus status,
                                      String json){

        response.setContentType("application/json");
        response.setStatus(status.value());
        try {
            PrintWriter printWriter =  response.getWriter();
            printWriter.println(json);
            printWriter.close();
        }
        catch (Exception exception){
            LogHelper.logError("Exception ", exception);
        }

    }
}
