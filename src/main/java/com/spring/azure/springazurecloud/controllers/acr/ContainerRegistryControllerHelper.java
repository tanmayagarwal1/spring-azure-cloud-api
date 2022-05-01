package com.spring.azure.springazurecloud.controllers.acr;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.dto.ContainerRegistryDto;
import com.spring.azure.springazurecloud.dto.ResponseDto;
import com.spring.azure.springazurecloud.exception.ApimException;
import com.spring.azure.springazurecloud.handlers.ResponseHandler;
import com.spring.azure.springazurecloud.utils.JsonUtil;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@UtilityClass
public class ContainerRegistryControllerHelper {
    public static ContainerRegistryDto handleContainerRegistryRequest(HttpServletRequest request){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        if(body.isEmpty()) throw new ApimException("Request Body is Empty");
        ContainerRegistryDto dto = JsonUtil.fromJson(body, ContainerRegistryDto.class).orElse(null);
        if(dto == null) throw new ApimException("DTO is NULL");
        return dto;
    }

    public static void handleContainerRegistryResponse(String info, HttpServletResponse response){
        ResponseDto responseDto = new ResponseDto(true);
        responseDto.addMessage("Info", info);
        ResponseHandler.handleResponse(response, HttpStatus.OK, responseDto);
    }
}
