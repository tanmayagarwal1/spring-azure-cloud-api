package com.spring.azure.springazurecloud.controllers.storage.account;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.dto.ResponseDto;
import com.spring.azure.springazurecloud.dto.StorageAccountRequestDto;
import com.spring.azure.springazurecloud.exception.StorageAccountException;
import com.spring.azure.springazurecloud.handlers.ResponseHandler;
import com.spring.azure.springazurecloud.utils.JsonUtil;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@UtilityClass
public class StorageAccountControllerHelper {
    public static StorageAccountRequestDto handleStorageAccountRequest(HttpServletRequest request){
        String body = JsonUtil.jsonFromRequest(request).orElse(Constants.PLACEHOLDER);
        if(body.isEmpty()) throw new StorageAccountException("Request Body is Empty");
        StorageAccountRequestDto dto = JsonUtil.fromJson(body, StorageAccountRequestDto.class).orElse(null);
        if(dto == null) throw new StorageAccountException("DTO is NULL");
        return dto;
    }

    public static void handleStorageAccountResponse(String info, HttpServletResponse response){
        ResponseDto responseDto = new ResponseDto(true);
        responseDto.addMessage("Info", info);
        ResponseHandler.handleResponse(response, HttpStatus.OK, responseDto);
    }
}
