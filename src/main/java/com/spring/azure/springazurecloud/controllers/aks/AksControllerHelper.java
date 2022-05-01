package com.spring.azure.springazurecloud.controllers.aks;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.dto.AksRequestDto;
import com.spring.azure.springazurecloud.dto.ResponseDto;
import com.spring.azure.springazurecloud.exception.AksException;
import com.spring.azure.springazurecloud.handlers.ResponseHandler;
import com.spring.azure.springazurecloud.utils.JsonUtil;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@UtilityClass
public class AksControllerHelper {
    public static AksRequestDto handleAksRequest(HttpServletRequest request){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        if(body.isEmpty()) throw new AksException("Request Body is Empty");
        AksRequestDto dto = JsonUtil.fromJson(body, AksRequestDto.class).orElse(null);
        if(dto == null) throw new AksException("DTO is NULL");
        return dto;
    }

    public static void handleAksResponse(String info, HttpServletResponse response){
        ResponseDto responseDto = new ResponseDto(true);
        responseDto.addMessage("Info", info);
        ResponseHandler.handleResponse(response, HttpStatus.OK, responseDto);
    }
}
