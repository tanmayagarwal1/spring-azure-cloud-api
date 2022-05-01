package com.spring.azure.springazurecloud.utils;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.dto.response.ResponseDto;
import com.spring.azure.springazurecloud.exception.general.RetrievalException;
import com.spring.azure.springazurecloud.exception.storage.StorageAccountException;
import com.spring.azure.springazurecloud.handlers.ResponseHandler;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@UtilityClass
public class GlobalControllerHelper {
    public static<T> T handleControllerRequest(HttpServletRequest request, Class<T> clazz){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        if(body.isEmpty()) throw new StorageAccountException("Request Body is Empty");
        T dto = JsonUtil.fromJson(body, clazz).orElse(null);
        if(dto == null) throw new StorageAccountException("DTO is NULL");
        return dto;
    }

    public static void handleControllerResponse(String info, HttpServletResponse response){
        ResponseDto responseDto = new ResponseDto(true);
        responseDto.addMessage("Info", info);
        ResponseHandler.handleResponse(response, HttpStatus.OK, responseDto);
    }

    public static<T> void handleModelControllerResponse(T object, HttpServletResponse response){
        String json = JsonUtil.toJson(object).orElse(null);
        if(json == null) throw new RetrievalException("Error retrieving "+object.getClass().getSimpleName());
        ResponseHandler.handleResponse(response,HttpStatus.OK,json);
    }
}
